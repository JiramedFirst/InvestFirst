package com.jjetawat.InvestFirst.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jjetawat.InvestFirst.model.Account;
import com.jjetawat.InvestFirst.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}
	
	@PostMapping("/{userId}")
	public Account addAccount(@PathVariable Long userId, @RequestBody Account account) {
		return accountService.createAccount(userId, account);
	}
}
