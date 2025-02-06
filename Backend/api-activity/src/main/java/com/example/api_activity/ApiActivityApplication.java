package com.example.api_activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class ApiActivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiActivityApplication.class, args);
	}

}
