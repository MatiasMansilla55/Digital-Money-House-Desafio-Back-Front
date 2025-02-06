package com.DigitalMoneyHouse.accountsservice.feignClient;

import com.DigitalMoneyHouse.accountsservice.dto.exit.ActivityOutDTO;
import com.DigitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.DigitalMoneyHouse.accountsservice.feignInterceptor.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "api-activity", url = "http://localhost:9091",configuration = FeignConfig.class)
public interface ApiActivityFeignClient {
    @GetMapping("/accounts/{accountId}/activity/{id}")
    ActivityOutDTO getActivityById(@PathVariable("id") Long id);

    @GetMapping("/accounts/{accountId}/activity")
    List<ActivityOutDTO> getAllActivitiesByAccountId(
            @PathVariable("accountId") Long accountId,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException;
    @GetMapping("/accounts/{accountId}/activity/{activityId}/receipt")
    ResponseEntity<byte[]> downloadActivityReceipt(@PathVariable Long accountId, @PathVariable Long activityId,@RequestHeader("Authorization") String token);

}
