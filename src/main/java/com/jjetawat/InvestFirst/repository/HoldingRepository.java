package com.jjetawat.InvestFirst.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jjetawat.InvestFirst.model.Holding;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long>{
	List<Holding> findByUserId(Long userId);
}
