package com.jjetawat.InvestFirst.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jjetawat.InvestFirst.dto.HoldingDTO;
import com.jjetawat.InvestFirst.dto.PortfolioSummaryDTO;
import com.jjetawat.InvestFirst.model.Account;
import com.jjetawat.InvestFirst.model.Holding;
import com.jjetawat.InvestFirst.model.TradeLog;
import com.jjetawat.InvestFirst.model.Transaction;
import com.jjetawat.InvestFirst.model.User;
import com.jjetawat.InvestFirst.repository.HoldingRepository;
import com.jjetawat.InvestFirst.repository.TradeLogRepository;
import com.jjetawat.InvestFirst.repository.TransactionRepository;
import com.jjetawat.InvestFirst.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PortfolioService {

	private final HoldingRepository holdingRepository;
	private final UserRepository userRepository;
	private final MarketDataService marketDataService;
	private final AccountService accountService;
	private final TransactionRepository transactionRepository;
	private final TradeLogRepository tradeLogRepository;
	
	public PortfolioService(UserRepository userRepository, HoldingRepository holdingRepository, MarketDataService marketDataService, AccountService accountService, TransactionRepository transactionRepository, TradeLogRepository tradeLogRepository) {
		super();
		this.holdingRepository = holdingRepository;
		this.userRepository = userRepository;
		this.marketDataService = marketDataService;
		this.accountService = accountService;
		this.transactionRepository = transactionRepository;
		this.tradeLogRepository = tradeLogRepository;
	}

	@Transactional
	public Holding addHolding(Long userId, Holding newHolding) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		BigDecimal totalCost = newHolding.getAveragePrice().multiply(newHolding.getQuantity());
		accountService.withdraw(userId, totalCost);

		Transaction bankTx = new Transaction();
	    bankTx.setAccount(user.getAccounts().get(0));
	    bankTx.setType("EXPENSE");
	    bankTx.setAmount(totalCost);
	    bankTx.setTimestemp(LocalDateTime.now());
	    bankTx.setDescription("Stock Purchase: " + newHolding.getSymbol());
	    transactionRepository.save(bankTx);
	    
	    TradeLog trade = new TradeLog();
	    trade.setUserId(userId);
	    trade.setSymbol(newHolding.getSymbol());
	    trade.setAction("BUY");
	    trade.setQuantity(newHolding.getQuantity());
	    trade.setPriceAtTrade(newHolding.getAveragePrice());
	    trade.setTotalCost(totalCost);
	    trade.setTimestamp(LocalDateTime.now());
	    tradeLogRepository.save(trade);
		
		return holdingRepository.findByUserId(userId).stream()
				.filter(h -> h.getSymbol().equalsIgnoreCase(newHolding.getSymbol()))
				.findFirst()
				.map(existingHolding ->{
					BigDecimal currentTotalCost = existingHolding.getAveragePrice().multiply(existingHolding.getQuantity());
				    
				    // 2. Calculate Cost of the new purchase
				    BigDecimal newPurchaseCost = newHolding.getAveragePrice().multiply(newHolding.getQuantity());
				    
				    // 3. New Total Quantity
				    BigDecimal totalQty = existingHolding.getQuantity().add(newHolding.getQuantity());
				    
				    // 4. Weighted Average Price = (Old Cost + New Cost) / Total Quantity
				    BigDecimal newAveragePrice = currentTotalCost.add(newPurchaseCost)
				            .divide(totalQty, 2, RoundingMode.HALF_UP);

				    existingHolding.setQuantity(totalQty);
				    existingHolding.setAveragePrice(newAveragePrice);
                    return holdingRepository.save(existingHolding);
				})
				.orElseGet(() -> {
					newHolding.setUser(user);
					return holdingRepository.save(newHolding);
				});
	}
	
	public PortfolioSummaryDTO getSummary(Long userId) {
        // 2. Find all assets the user owns
        List<Holding> holdings = holdingRepository.findByUserId(userId);

        // 3. Map each "Holding" (DB data) to a "HoldingDTO" (Live data)
        List<HoldingDTO> assetList = holdings.stream().map(holding -> {
            BigDecimal currentPrice = marketDataService.getLivePrice(holding.getSymbol());
            BigDecimal marketValue = currentPrice.multiply(holding.getQuantity()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal costBasis = holding.getAveragePrice().multiply(holding.getQuantity());
            BigDecimal profitLoss = marketValue.subtract(costBasis).setScale(2, RoundingMode.HALF_UP);

            return new HoldingDTO(
                holding.getSymbol(),
                holding.getQuantity(),
                holding.getAveragePrice(),
                currentPrice,
                marketValue,
                profitLoss
            );
        }).collect(Collectors.toList());

        // 4. Calculate Totals for the Dashboard
        BigDecimal totalValue = assetList.stream()
                .map(HoldingDTO::getMarketValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPL = assetList.stream()
                .map(HoldingDTO::getProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PortfolioSummaryDTO(totalValue, totalPL, assetList);
    }
	
	@Transactional
	public Holding sellHolding(Long userId, String symbol, BigDecimal quantityToSell) {
	    Holding existing = holdingRepository.findByUserId(userId).stream()
	            .filter(h -> h.getSymbol().equalsIgnoreCase(symbol))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("You do not own this stock!"));

	    if (existing.getQuantity().compareTo(quantityToSell) < 0) {
	        throw new RuntimeException("Not enough shares to sell! You only have: " + existing.getQuantity());
	    }
	    
	    BigDecimal currentPrice = marketDataService.getLivePrice(symbol);
	    BigDecimal saleProceeds = currentPrice.multiply(quantityToSell); 
	    accountService.deposit(userId, saleProceeds);

	    Transaction bankTx = new Transaction();
	    bankTx.setAccount(existing.getUser().getAccounts().get(0)); // Link to user's account
	    bankTx.setType("INCOME"); // Selling puts money IN
	    bankTx.setAmount(saleProceeds);
	    bankTx.setTimestemp(LocalDateTime.now());
	    bankTx.setDescription("Stock Sale: " + symbol + " (" + quantityToSell + " shares)");
	    transactionRepository.save(bankTx);

	    // 6. Log the STOCK Trade (Trade history record)
	    TradeLog trade = new TradeLog();
	    trade.setUserId(userId);
	    trade.setSymbol(symbol);
	    trade.setAction("SELL");
	    trade.setQuantity(quantityToSell);
	    trade.setPriceAtTrade(currentPrice);
	    trade.setTotalCost(saleProceeds);
	    trade.setTimestamp(LocalDateTime.now());
	    tradeLogRepository.save(trade);
	    
	    // Reduce the quantity
	    existing.setQuantity(existing.getQuantity().subtract(quantityToSell));

	    // If quantity hits 0, remove it from the portfolio entirely
	    if (existing.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
	        holdingRepository.delete(existing);
	        return null;
	    }

	    return holdingRepository.save(existing);
	}
	
}
