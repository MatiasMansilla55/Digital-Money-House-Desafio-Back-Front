package com.example.api_cards.exceptions;

import java.nio.file.FileAlreadyExistsException;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(String file) {
        super(file);
    }
}
