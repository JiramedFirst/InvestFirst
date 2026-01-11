package com.jjetawat.InvestFirst.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jjetawat.InvestFirst.dto.UserDashboardDTO;
import com.jjetawat.InvestFirst.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {
	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		super();
		this.dashboardService = dashboardService;
	}

	@GetMapping("/{userId}")
	public UserDashboardDTO getFullDashboard(@PathVariable Long userId) {
        return dashboardService.getUserDashboard(userId);
    }
}
