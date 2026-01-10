package com.jjetawat.InvestFirst.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jjetawat.InvestFirst.model.TradeLog;

public interface TradeLogRepository extends JpaRepository<TradeLog, Long> {
	List<TradeLog> findByUserIdOrderByTimestampDesc(Long userId);

}
