package com.DigitalMoneyHouse.accountsservice.dto;


import lombok.Data;

import java.math.BigDecimal;

public class AccountResponse {
    private Long Id;
    private BigDecimal balance;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
