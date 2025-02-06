package com.example.api_cards.exceptions;

import jakarta.ws.rs.NotFoundException;

public class CardNotFoundException extends NotFoundException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
