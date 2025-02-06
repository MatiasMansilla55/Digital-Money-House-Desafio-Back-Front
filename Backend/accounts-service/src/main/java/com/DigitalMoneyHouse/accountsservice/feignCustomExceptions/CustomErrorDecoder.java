package com.DigitalMoneyHouse.accountsservice.feignCustomExceptions;

import com.DigitalMoneyHouse.accountsservice.exceptions.BadRequestException;
import com.DigitalMoneyHouse.accountsservice.exceptions.CardAlreadyExistsException;
import com.DigitalMoneyHouse.accountsservice.exceptions.ConflictException;
import com.DigitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException("Bad request, check information");
            case 404:
                return new ResourceNotFoundException("Resource not found");
            case 409:
                // Puedes analizar el contenido del cuerpo o encabezados para decidir la excepción específica
                String responseBody = response.body() != null ? response.body().toString() : "";
                if (responseBody.contains("Card already exists")) {
                    return new CardAlreadyExistsException("The card already exists for the account");
                }
                return new ConflictException("Resource already exists");
            default:
                return new Exception("Try again later");
        }
    }
}

