package com.example.api_activity.feignInterceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Configuration
@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String jwtToken = (String) authentication.getCredentials();
            if (jwtToken != null) {
                template.header("Authorization", "Bearer " + jwtToken);
                System.out.println("Token añadido a la cabecera: " + jwtToken);
            } else {
                System.out.println("El token en el SecurityContextHolder es nulo.");
            }
        } else {
            System.out.println("No hay autenticación válida en el SecurityContextHolder.");
        }
    }

}
