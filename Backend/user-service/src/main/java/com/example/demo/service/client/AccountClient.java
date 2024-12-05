package com.example.demo.service.client;

import com.example.demo.dto.entry.AccountCreationRequest;
import com.example.demo.dto.entry.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(name = "accounts-service",url= "http://localhost:8084")
public interface AccountClient {
    @PostMapping("/accounts/create")
    AccountResponse createAccount(AccountCreationRequest request);
}
