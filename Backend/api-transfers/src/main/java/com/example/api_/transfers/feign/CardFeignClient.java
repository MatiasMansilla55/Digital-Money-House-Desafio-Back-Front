package com.example.api_.transfers.feign;


import com.example.api_.transfers.dto.entry.Card;
import com.example.api_.transfers.feignInterceptor.FeignConfig;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "api-cards", url = "http://localhost:8083",  configuration = FeignConfig.class)
public interface CardFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/accounts/{accountId}/cards/feign/{cardId}")
     Optional<Card> getCardtById(@PathVariable("accountId") Long accountId,@PathVariable("cardId") Long cardId,@RequestHeader("Authorization") String token);

}
