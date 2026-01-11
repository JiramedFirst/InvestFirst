package com.jjetawat.InvestFirst.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jjetawat.InvestFirst.model.Transaction;
import com.jjetawat.InvestFirst.service.TransactionService;
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

	private final TransactionService transactionService; 
	
	public TransactionController(TransactionService transactionService) {
		super();
		this.transactionService = transactionService;	
	}
	
	@PostMapping("/{accountId}")
	public Transaction addTransaction(@PathVariable Long accountId,@RequestBody Transaction transaction) {
		return transactionService.recoredTransaction(accountId, transaction);
	}
	
	@GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
	
}
