package com.DigitalMoneyHouse.accountsservice.feignInterceptor;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor feignClientInterceptor() {
        return new JwtTokenPropagator();
    }
}
