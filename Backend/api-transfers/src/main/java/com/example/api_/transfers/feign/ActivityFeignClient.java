package com.example.api_.transfers.feign;


import com.example.api_.transfers.dto.entry.Activity;
import com.example.api_.transfers.feignInterceptor.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-activity", url = "http://localhost:9091", configuration = FeignConfig.class)
public interface ActivityFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/accounts/{accountId}/activity/save")
    Activity save(@RequestBody Activity activity, @PathVariable("accountId") Long accountId, @RequestHeader("Authorization") String token);
}
