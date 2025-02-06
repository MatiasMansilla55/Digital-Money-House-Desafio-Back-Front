package com.example.api_cards.service;

import com.example.api_cards.dto.entry.CreateCardEntryDTO;
import com.example.api_cards.dto.exit.CardOutDTO;
import com.example.api_cards.exceptions.CardAlreadyExistsException;
import com.example.api_cards.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ICardService {
    List<CardOutDTO> getCardsByAccountId(Long accountId);
    CardOutDTO getCardById(Long accountId, Long cardId) throws ResourceNotFoundException;
    CardOutDTO createCard(Long accountId, CreateCardEntryDTO createCardEntryDTO, String token) throws CardAlreadyExistsException, ResourceNotFoundException;
    void deleteCard(Long accountId, Long cardId);
}
