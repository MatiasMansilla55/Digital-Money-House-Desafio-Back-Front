package com.example.api_cards.controller;

import com.example.api_cards.dto.entry.CreateCardEntryDTO;
import com.example.api_cards.dto.exit.CardOutDTO;
import com.example.api_cards.dto.exit.TransferenceOutDTO;
import com.example.api_cards.entities.Card;
import com.example.api_cards.exceptions.CardAlreadyExistsException;
import com.example.api_cards.exceptions.CardNotFoundException;
import com.example.api_cards.exceptions.ResourceNotFoundException;
import com.example.api_cards.exceptions.UnauthorizedException;
import com.example.api_cards.feign.TransfersFeignClient;
import com.example.api_cards.repository.CardRepository;
import com.example.api_cards.service.impl.CardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/cards")
public class CardController {

    @Autowired
    private CardServiceImpl cardServiceImpl;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransfersFeignClient transfersFeignClient;

    @Operation(summary = "Obtener todas las tarjetas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarjetas obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CardOutDTO>> getAllCards(@PathVariable Long accountId) {
        List<CardOutDTO> cards = cardServiceImpl.getCardsByAccountId(accountId);
        if (cards.isEmpty()) {
            return ResponseEntity.ok().body(cards);
        }
        return ResponseEntity.ok(cards);
    }
    @Operation(summary = "Obtener tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarjeta obtenida con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/{cardId}")
    public ResponseEntity<CardOutDTO> getCard(@PathVariable Long accountId, @PathVariable Long cardId) throws ResourceNotFoundException {
        CardOutDTO card = cardServiceImpl.getCardById(accountId, cardId);
        return ResponseEntity.ok(card);
    }
    @Operation(summary = "Crear tarjeta de credito/debito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarjeta creada con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CardOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createCard(
            @PathVariable Long accountId,
            @Valid @RequestBody CreateCardEntryDTO createCardEntryDTO,
            @RequestHeader("Authorization") String token) {
        try {
            // Validar token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String jwtToken = token.replace("Bearer ", "");

            // Lógica del servicio
            CardOutDTO newCard = cardServiceImpl.createCard(accountId, createCardEntryDTO, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCard);

        } catch (CardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @Operation(summary = "Eliminar tarjeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "tarjeta eliminada con exito"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @DeleteMapping("{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long accountId, @PathVariable Long cardId) {
        try {
            cardServiceImpl.deleteCard(accountId, cardId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Tarjeta eliminada exitosamente"));
        } catch (CardNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    //Endpont para Feign
    @GetMapping("/feign/{cardId}")
    public ResponseEntity<Card> getCardForFeign(@PathVariable Long cardId,@RequestHeader("Authorization") String token) {
        return cardRepository.findById(cardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
    @PostMapping("/cards")
    public ResponseEntity<String> registerTransference(
            @PathVariable Long accountId,
            @RequestBody TransferenceOutDTO transferenceOutDto, @RequestHeader("Authorization") String token) {

        try {
            transfersFeignClient.registerTransference(accountId, transferenceOutDto,"Bearer "+token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso registrado con éxito");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta o tarjeta no encontrada");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permisos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el ingreso");
        }
    }
            */

}