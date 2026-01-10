package com.jjetawat.InvestFirst.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

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

        // 2. Get Total Stock Value from PortfolioService
        BigDecimal totalStockValue = portfolioService.getSummary(userId).getTotalPortfolioValue();

        // 3. Combine them
        BigDecimal netWorth = totalCash.add(totalStockValue);

        return new UserDashboardDTO(user.getFullname(), totalCash, totalStockValue, netWorth);
    }
	
}
