package com.example.api_cards.feign;

import com.example.api_cards.dto.exit.TransferenceOutDTO;
import com.example.api_cards.exceptions.ResourceNotFoundException;
import com.example.api_cards.feignInterceptor.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "api-transfers", url = "http://localhost:9092", configuration = FeignConfig.class)
public interface TransfersFeignClient {
    @PostMapping("/accounts/{accountId}/transferences/cards")
    ResponseEntity<String> registerTransference(@PathVariable Long accountId, @RequestBody TransferenceOutDTO transferenceOutDto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException;
}
