package com.example.api_cards;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Imprime información del error
        System.err.println("Error Feign: " + response.status() + " - " + response.reason());
        return FeignException.errorStatus(methodKey, response);
    }
}
