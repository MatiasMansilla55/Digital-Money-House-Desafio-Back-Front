package com.example.api_activity.feign;


import com.example.api_activity.dto.entry.Account;
import com.example.api_activity.dto.exit.AccountOutDTO;
import com.example.api_activity.feignInterceptor.FeignConfig;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "accounts-service", url = "http://localhost:8084" ,configuration = FeignConfig.class)
public interface AccountFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/accounts/{id}")
    Optional<AccountOutDTO> getAccountById(@PathVariable Long id, @RequestHeader("Authorization") String token);


    @RequestMapping(method = RequestMethod.GET, value = "/accounts/email")
    Account findByEmail(@RequestParam("email") String email, @RequestHeader("Authorization") String token);
}
