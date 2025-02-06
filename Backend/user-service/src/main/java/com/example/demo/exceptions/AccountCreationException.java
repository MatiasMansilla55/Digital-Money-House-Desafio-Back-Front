package com.example.demo.exceptions;

public class AccountCreationException extends RuntimeException{
    public AccountCreationException(String message) {
        super(message);
    }
}
