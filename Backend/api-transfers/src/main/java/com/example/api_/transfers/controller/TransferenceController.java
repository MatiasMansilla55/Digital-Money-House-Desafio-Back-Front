package com.example.api_.transfers.controller;

import com.example.api_.transfers.dto.exit.TransferRequestOutDTO;
import com.example.api_.transfers.dto.exit.TransferenceOutDTO;
import com.example.api_.transfers.entities.Transference;
import com.example.api_.transfers.exceptions.InsufficientFundsException;
import com.example.api_.transfers.exceptions.ResourceNotFoundException;
import com.example.api_.transfers.exceptions.UnauthorizedException;
import com.example.api_.transfers.service.impl.TransferenceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/transferences")
public class TransferenceController {

    @Autowired
    private TransferenceServiceImpl transferenceServiceImpl;
    @Operation(summary = "Registro de transferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferencia registrada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Transferencia registrada con éxito"))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/cards")
    public ResponseEntity<String> registerTransference(
            @PathVariable Long accountId,
            @RequestBody TransferenceOutDTO transferenceOutDto,@RequestHeader("Authorization") String token) {

        try {
            transferenceServiceImpl.registerTransferenceFromCards(accountId, transferenceOutDto,token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso registrado con éxito");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta o tarjeta no encontrada");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permisos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el ingreso");
        }
    }
    @Operation(summary = "Transferencia con efectivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferencia realizada con exito"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/money")
    public ResponseEntity<?> makeTransfer(
            @PathVariable Long accountId,
            @RequestBody TransferRequestOutDTO transferRequest,@RequestHeader("Authorization") String token) {
        try {
            // Realizar la transferencia
            transferenceServiceImpl.makeTransferFromCash(accountId, transferRequest, token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transferencia realizada con éxito");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cuenta inexistente");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.GONE).body("Fondos insuficientes");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Permisos insuficientes");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el procesamiento de la transferencia");
        }
    }
    @Operation(summary = "Obtener ultimas transferencias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transferencias obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transference.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/last-transferred-accounts")
    public ResponseEntity<List<Transference>> getLastTransferredAccounts(@PathVariable Long accountId) {
        List<Transference> lastTransfers = transferenceServiceImpl.getLastTransferredAccounts(accountId);
        return ResponseEntity.ok(lastTransfers);
    }
}