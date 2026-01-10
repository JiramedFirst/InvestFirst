package com.jjetawat.InvestFirst.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MarketDataService {
	
	private final WebClient webClient;
	private final String API_KEY = "d5h8c9hr01qqequ0up3gd5h8c9hr01qqequ0up40";
	
	public MarketDataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://finnhub.io/api/v1").build();
    }
	
	public BigDecimal getLivePrice(String symbol) {
		try {
			Map response = this.webClient.get()
					.uri(UriBuilder -> UriBuilder
							.path("/quote")
							.queryParam("symbol", symbol)
							.queryParam("token", API_KEY)
							.build())
					.retrieve()
					.bodyToMono(Map.class)
					.block();
			Object price = response.get("c");
			return new BigDecimal(price.toString());
		}catch (Exception e) {
			System.err.println("Error fetching price: " + e.getMessage());
            return BigDecimal.ZERO;
		}
	}
}
