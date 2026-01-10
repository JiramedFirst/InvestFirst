package com.jjetawat.InvestFirst.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDashboardDTO {

	private String fullname;
    private BigDecimal totalCashBalance;
    private BigDecimal totalStockValue;
    private BigDecimal netWorth;
    
	public UserDashboardDTO(String fullname, BigDecimal totalCashBalance, BigDecimal totalStockValue,
			BigDecimal netWorth) {
		super();
		this.fullname = fullname;
		this.totalCashBalance = totalCashBalance;
		this.totalStockValue = totalStockValue;
		this.netWorth = netWorth;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public BigDecimal getTotalCashBalance() {
		return totalCashBalance;
	}

	public void setTotalCashBalance(BigDecimal totalCashBalance) {
		this.totalCashBalance = totalCashBalance;
	}

	public BigDecimal getTotalStockValue() {
		return totalStockValue;
	}

	public void setTotalStockValue(BigDecimal totalStockValue) {
		this.totalStockValue = totalStockValue;
	}

	public BigDecimal getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(BigDecimal netWorth) {
		this.netWorth = netWorth;
	}
    
    
}
