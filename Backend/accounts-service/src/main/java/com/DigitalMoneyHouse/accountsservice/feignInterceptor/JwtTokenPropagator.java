package com.DigitalMoneyHouse.accountsservice.feignInterceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class JwtTokenPropagator implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // Retrieve the JWT token from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String token = (String) authentication.getCredentials();
            template.header("Authorization", "Bearer " + token);
        }
    }
}
