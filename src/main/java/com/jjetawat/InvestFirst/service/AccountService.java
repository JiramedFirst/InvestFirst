package com.jjetawat.InvestFirst.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jjetawat.InvestFirst.model.Account;
import com.jjetawat.InvestFirst.model.User;
import com.jjetawat.InvestFirst.repository.AccountRepository;
import com.jjetawat.InvestFirst.repository.UserRepository;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	
	public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
		super();
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
	}

	public Account createAccount(long userId, Account account) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		account.setUser(user);
		return accountRepository.save(account);
	}
	
	public void deposit(Long userId, BigDecimal amount) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
	    Account account = accountRepository.findByUser(user)
	            .stream().findFirst()
	            .orElseThrow(() -> new RuntimeException("Account not found"));
	    
	    account.setBalance(account.getBalance().add(amount));
	    accountRepository.save(account);
	}
	
	public void withdraw(Long userId, BigDecimal amount) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
	    Account account = accountRepository.findByUser(user)
	            .stream().findFirst()
	            .orElseThrow(() -> new RuntimeException("Account not found"));

	    // Check if the user is broke!
	    if (account.getBalance().compareTo(amount) < 0) {
	        throw new RuntimeException("Insufficient funds! You need $" + amount + " but only have $" + account.getBalance());
	    }

	    account.setBalance(account.getBalance().subtract(amount));
	    accountRepository.save(account);
	}
}
