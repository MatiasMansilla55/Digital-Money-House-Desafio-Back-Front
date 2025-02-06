package com.example.api_cards.repository;

import com.example.api_cards.dto.entry.CreateCardEntryDTO;
import com.example.api_cards.dto.exit.CardOutDTO;
import com.example.api_cards.entities.Card;
import com.example.api_cards.exceptions.CardAlreadyExistsException;
import com.example.api_cards.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountId(Long accountId); // Obtener tarjetas por ID de cuenta
    Optional<Card> findByNumber(String number);
}
