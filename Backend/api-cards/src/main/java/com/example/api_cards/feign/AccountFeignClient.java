package com.example.api_cards.feign;


import com.example.api_cards.dto.Account;
import com.example.api_cards.feignInterceptor.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "accounts-service", url = "http://localhost:8084/accounts", configuration = FeignConfig.class)
public interface AccountFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    Account getAccountById(@PathVariable("id") Long id,@RequestHeader("Authorization") String token);


    @RequestMapping(method = RequestMethod.GET, value = "/email")
    Account findByEmail(@RequestParam("email") String email, @RequestHeader("Authorization") String token);
}
