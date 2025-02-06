package com.DigitalMoneyHouse.accountsservice.feignClient;

import com.DigitalMoneyHouse.accountsservice.dto.entry.CreateCardEntryDTO;

import com.DigitalMoneyHouse.accountsservice.feignInterceptor.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "api-cards", url = "http://localhost:8083",configuration = FeignConfig.class)
public interface ApiCardFeignClient {
    @PostMapping("/accounts/{accountId}/cards")
    ResponseEntity<?> createCard(
            @PathVariable("accountId") Long accountId,
            @RequestBody CreateCardEntryDTO createCardEntryDTO,
            @RequestHeader("Authorization") String token);
}
