package com.example.demo.service.client;

import com.example.demo.dto.entry.AccountCreationRequest;
import com.example.demo.dto.entry.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "accounts-service",url = "http://localhost:8084/accounts")
public interface AccountClient {
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    AccountResponse createAccount(@RequestBody AccountCreationRequest request);
}
