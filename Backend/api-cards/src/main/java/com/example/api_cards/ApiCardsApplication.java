package com.example.api_cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
public class ApiCardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCardsApplication.class, args);
	}

}
