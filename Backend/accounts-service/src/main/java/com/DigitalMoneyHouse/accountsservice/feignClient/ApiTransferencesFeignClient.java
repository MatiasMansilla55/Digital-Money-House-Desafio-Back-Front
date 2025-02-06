package com.DigitalMoneyHouse.accountsservice.feignClient;

import com.DigitalMoneyHouse.accountsservice.dto.exit.TransferRequestOutDTO;
import com.DigitalMoneyHouse.accountsservice.dto.exit.TransferenceOutDTO;
import com.DigitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.DigitalMoneyHouse.accountsservice.feignInterceptor.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@FeignClient(name = "api-transfers", url = "http://localhost:9092",configuration = FeignConfig.class)
public interface ApiTransferencesFeignClient {

@RequestMapping(method = RequestMethod.POST, value ="/accounts/{accountId}/transferences/cards")
void registerTransference(@PathVariable Long accountId, @RequestBody TransferenceOutDTO transferenceOutDto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException;

@RequestMapping(method = RequestMethod.POST, value ="/accounts/{accountId}/transferences/money")
void makeTransfer(@PathVariable Long accountId, @RequestBody TransferRequestOutDTO transferRequest, @RequestHeader("Authorization") String token)throws AccountNotFoundException;

}
