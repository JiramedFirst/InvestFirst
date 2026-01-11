package com.jjetawat.InvestFirst.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jjetawat.InvestFirst.dto.HoldingDTO;
import com.jjetawat.InvestFirst.dto.PortfolioSummaryDTO;
import com.jjetawat.InvestFirst.dto.UserDashboardDTO;
import com.jjetawat.InvestFirst.model.Account;
import com.jjetawat.InvestFirst.model.User;
import com.jjetawat.InvestFirst.repository.UserRepository;

@Service
public class DashboardService {
	
	private final UserRepository userRepository;
	private final PortfolioService portfolioService;
	
	public DashboardService(UserRepository userRepository, PortfolioService portfolioService) {
		super();
		this.userRepository = userRepository;
		this.portfolioService = portfolioService;
	}
	
	public UserDashboardDTO getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Calculate Total Cash from all accounts
        BigDecimal totalCash = user.getAccounts().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Get the Portfolio Summary object
        PortfolioSummaryDTO portfolioSummary = portfolioService.getSummary(userId);

        // 3. Extract the value and the list of holdings from that object
        BigDecimal totalStockValue = portfolioSummary.getTotalPortfolioValue();
        List<HoldingDTO> assets = portfolioSummary.getHoldings(); // Corrected reference
        
        // 4. Combine for Net Worth
        BigDecimal netWorth = totalCash.add(totalStockValue);

        return new UserDashboardDTO(user.getFullname(), totalCash, totalStockValue, netWorth,assets);
    }
	
}
