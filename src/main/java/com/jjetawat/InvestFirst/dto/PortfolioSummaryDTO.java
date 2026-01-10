package com.jjetawat.InvestFirst.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSummaryDTO {
	private BigDecimal totalPortfolioValue; // Sum of all marketValues
    private BigDecimal totalProfitLoss;     // Sum of all profitLoss
    private List<HoldingDTO> assets;        // The detailed list
    
	public PortfolioSummaryDTO(BigDecimal totalPortfolioValue, BigDecimal totalProfitLoss, List<HoldingDTO> assets) {
		super();
		this.totalPortfolioValue = totalPortfolioValue;
		this.totalProfitLoss = totalProfitLoss;
		this.assets = assets;
	}

	public BigDecimal getTotalPortfolioValue() {
		return totalPortfolioValue;
	}

	public void setTotalPortfolioValue(BigDecimal totalPortfolioValue) {
		this.totalPortfolioValue = totalPortfolioValue;
	}

	public BigDecimal getTotalProfitLoss() {
		return totalProfitLoss;
	}

	public void setTotalProfitLoss(BigDecimal totalProfitLoss) {
		this.totalProfitLoss = totalProfitLoss;
	}

	public List<HoldingDTO> getAssets() {
		return assets;
	}

	public void setAssets(List<HoldingDTO> assets) {
		this.assets = assets;
	}
    
    
	
}
