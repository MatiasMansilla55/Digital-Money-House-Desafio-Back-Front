package com.DigitalMoneyHouse.accountsservice.exceptions;

import jakarta.ws.rs.NotFoundException;

public class AccountPersistenceException extends NotFoundException {
    public AccountPersistenceException(String file) {
        super(file);
    }
}
