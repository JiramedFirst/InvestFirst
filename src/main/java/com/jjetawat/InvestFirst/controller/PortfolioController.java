package com.jjetawat.InvestFirst.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jjetawat.InvestFirst.dto.PortfolioSummaryDTO;
import com.jjetawat.InvestFirst.model.Holding;
import com.jjetawat.InvestFirst.service.PortfolioService;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {
	private final PortfolioService portfolioService;

	public PortfolioController(PortfolioService portfolioService) {
		super();
		this.portfolioService = portfolioService;
	}
	
	@PostMapping("/{userId}/buy")
	public Holding buyAsset(@PathVariable long userId,@RequestBody Holding holding) {
		return portfolioService.addHolding(userId, holding);
	}
	
	@PostMapping("/{userId}/sell")
	public ResponseEntity<?> sellStock(
	    @PathVariable Long userId, 
	    @RequestParam String symbol, 
	    @RequestParam BigDecimal quantity
	) {
	    try {
	        Holding updatedHolding = portfolioService.sellHolding(userId, symbol, quantity);
	        
	        if (updatedHolding == null) {
	            return ResponseEntity.ok("Position closed. All shares sold.");
	        }
	        
	        return ResponseEntity.ok(updatedHolding);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}
	
	@GetMapping("/{userId}/summary")
	public PortfolioSummaryDTO getSummary(@PathVariable Long userId) {
	    return portfolioService.getSummary(userId);
	}
}
