package com.jjetawat.InvestFirst.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoldingDTO {
	private String symbol;
    private BigDecimal quantity;
    private BigDecimal averagePrice;  // What you paid
    private BigDecimal currentPrice;  // Real-time from API
    private BigDecimal marketValue;   // quantity * currentPrice
    private BigDecimal profitLoss;		// marketValue - (quantity * averagePrice)
    
	public HoldingDTO(String symbol, BigDecimal quantity, BigDecimal averagePrice, BigDecimal currentPrice,
			BigDecimal marketValue, BigDecimal profitLoss) {
		super();
		this.symbol = symbol;
		this.quantity = quantity;
		this.averagePrice = averagePrice;
		this.currentPrice = currentPrice;
		this.marketValue = marketValue;
		this.profitLoss = profitLoss;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public BigDecimal getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(BigDecimal marketValue) {
		this.marketValue = marketValue;
	}

	public BigDecimal getProfitLoss() {
		return profitLoss;
	}

	public void setProfitLoss(BigDecimal profitLoss) {
		this.profitLoss = profitLoss;
	}
    
	
    
}
