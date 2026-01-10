package com.jjetawat.InvestFirst.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jjetawat.InvestFirst.model.Account;
import com.jjetawat.InvestFirst.model.Transaction;
import com.jjetawat.InvestFirst.repository.AccountRepository;
import com.jjetawat.InvestFirst.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	
	public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		super();
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}
	
	@Transactional
	public Transaction recoredTransaction(Long accountId, Transaction transaction) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found"));
		
		if("EXPENSE".equalsIgnoreCase(transaction.getType())) {
			account.setBalance(account.getBalance().subtract(transaction.getAmount()));
		} else {
			account.setBalance(account.getBalance().add(transaction.getAmount()));
		}
		
		transaction.setAccount(account);
		transaction.setTimestemp(LocalDateTime.now());
		
		accountRepository.save(account);
		return transactionRepository.save(transaction);
	}
	
	public Transaction getTransaction(Long accountId) {
		return transactionRepository.getReferenceById(accountId);
	}
}
