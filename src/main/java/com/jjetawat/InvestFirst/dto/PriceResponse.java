package com.jjetawat.InvestFirst.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceResponse {
	private String symbol;
	private BigDecimal currentPrice;
	
	public PriceResponse(String symbol, BigDecimal currentPrice) {
		super();
		this.symbol = symbol;
		this.currentPrice = currentPrice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
	
}
