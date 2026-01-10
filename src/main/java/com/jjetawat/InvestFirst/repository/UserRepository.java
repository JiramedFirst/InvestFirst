package com.jjetawat.InvestFirst.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jjetawat.InvestFirst.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	

}
