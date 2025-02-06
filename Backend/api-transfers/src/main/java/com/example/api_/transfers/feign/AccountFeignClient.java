package com.example.api_.transfers.feign;


import com.example.api_.transfers.dto.entry.Account;
import com.example.api_.transfers.feignInterceptor.FeignConfig;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "accounts-service", url = "http://localhost:8084/accounts",configuration = FeignConfig.class)
public interface AccountFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    Account getAccountById(@PathVariable("id") Long id,@RequestHeader("Authorization") String token);


    @RequestMapping(method = RequestMethod.GET, value = "/email")
    Account findByEmail(@RequestParam("email") String email,@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/save")
    Account saveAccount(@RequestBody Account account,@PathVariable("accountId") Long accountId,@RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/findByAlias/{alias}")
    Account findByAlias(@PathVariable("alias") String alias, @RequestHeader("Authorization") String token);


    @RequestMapping(method = RequestMethod.GET, value = "/findByCvu/{cvu}")
    Account findByCvu(@PathVariable("cvu") String cvu,@RequestHeader("Authorization") String token);
}
