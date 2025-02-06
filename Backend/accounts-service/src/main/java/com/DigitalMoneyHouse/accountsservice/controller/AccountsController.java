package com.DigitalMoneyHouse.accountsservice.controller;


import com.DigitalMoneyHouse.accountsservice.dto.AccountCreationRequest;
import com.DigitalMoneyHouse.accountsservice.dto.AccountResponse;
import com.DigitalMoneyHouse.accountsservice.dto.AccountUpdateRequest;
import com.DigitalMoneyHouse.accountsservice.dto.entry.AccountEntryDTO;
import com.DigitalMoneyHouse.accountsservice.dto.entry.CreateCardEntryDTO;
import com.DigitalMoneyHouse.accountsservice.dto.exit.*;
import com.DigitalMoneyHouse.accountsservice.entities.Account;
import com.DigitalMoneyHouse.accountsservice.entities.Activity;
import com.DigitalMoneyHouse.accountsservice.entities.Transaction;
import com.DigitalMoneyHouse.accountsservice.exceptions.*;
import com.DigitalMoneyHouse.accountsservice.feignClient.ApiActivityFeignClient;
import com.DigitalMoneyHouse.accountsservice.feignClient.ApiCardFeignClient;
import com.DigitalMoneyHouse.accountsservice.feignClient.ApiTransferencesFeignClient;
import com.DigitalMoneyHouse.accountsservice.repository.AccountsRepository;
import com.DigitalMoneyHouse.accountsservice.service.impl.AccountsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountNotFoundException;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountsController {
    @Autowired
    private AccountsServiceImpl accountsServiceImpl;
    private final AccountsRepository accountsRepository;
    private final WebClient webClient;
    @Autowired
    private ApiActivityFeignClient apiActivityFeignClient;
    @Autowired
    private ApiCardFeignClient apiCardFeignClient;
    @Autowired
    private ApiTransferencesFeignClient apiTransferencesFeignClient;

    @Autowired
    public AccountsController(AccountsRepository accountsRepository, WebClient.Builder webClientBuilder, ApiActivityFeignClient apiActivityFeignClient,ApiTransferencesFeignClient apiTransferencesFeignClient,ApiCardFeignClient apiCardFeignClient) {
        this.accountsRepository = accountsRepository;
        this.webClient = webClientBuilder.baseUrl("http://gateway:8084").build();
        this.apiTransferencesFeignClient=apiTransferencesFeignClient;
        this.apiCardFeignClient=apiCardFeignClient;
        this.apiActivityFeignClient=apiActivityFeignClient;

    }
    @Operation(summary = "Creacion y registro de una nueva cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountCreationRequest request) {
        AccountResponse response = accountsServiceImpl.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> getAccountSummary(@PathVariable Long id) throws ResourceNotFoundException {
        AccountResponse accountResponse = accountsServiceImpl.getAccountSummary(id);
        return ResponseEntity.ok(accountResponse);
    }
    @Operation(summary = "Obtener las ultimas transacciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacciones obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getLastTransactions(@PathVariable Long id) {
        List<Transaction> transactions = accountsServiceImpl.getLastTransactions(id);
        return ResponseEntity.ok(transactions);
    }
    @Operation(summary = "Obtener Todas las Cuentas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuentas Obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })

    @GetMapping
    public ResponseEntity<List<AccountOutDTO>> getAllAccounts() {
        List<AccountOutDTO> accounts = accountsServiceImpl.getAccounts();
        return ResponseEntity.ok(accounts);
    }
    @Operation(summary = "Obtener Cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta obtenida con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })

    @GetMapping("/{id}")
    public ResponseEntity<AccountOutDTO> getAccount(@PathVariable Long id,@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        AccountOutDTO account = accountsServiceImpl.getAccountById(id);
        return ResponseEntity.ok(account);
    }
    @Operation(summary = "Actualizar Cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuentas actualizada con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AccountOutDTO> updateAccount(@PathVariable Long id, @RequestBody AccountEntryDTO accountEntryDTO) throws ResourceNotFoundException {
        AccountOutDTO updatedAccount = accountsServiceImpl.updateAccount(id, accountEntryDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    @PatchMapping("/update/alias/{id}")
    public Mono<ResponseEntity<Map<String, String>>> updateAlias(@PathVariable Long id, @RequestBody AccountUpdateRequest request) {
        accountsRepository.updateAlias(id, request.getAlias());
        return webClient.patch()
                .uri("http://localhost:8085/users/update/alias/{id}", id)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(Collections.singletonMap("message", "Alias actualizado exitosamente")))
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(500).body(Collections.singletonMap("error", "Error al actualizar el alias en el servicio de usuarios: " + e.getMessage())));
                });
    }
    @Operation(summary = "Obtener Todas las actividades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividades Obtenidas con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ActivityOutDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })

    //Endopoints para Feign:
    @GetMapping("/email")
    public ResponseEntity<Account> getAccountSummary(@RequestParam String email, @RequestHeader("Authorization") String token)  {
        Account accountResponse = accountsServiceImpl.findByEmail(email,token);
        return ResponseEntity.ok(accountResponse);
    }

    @PostMapping("/{accountId}/save")
    public ResponseEntity<Account> saveAccount(@RequestBody Account account,@PathVariable Long accountId, @RequestHeader("Authorization") String token) {
        Account savedAccount = accountsRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }
    /**
     * Endpoint para encontrar una cuenta por alias
     */

    @GetMapping("/findByAlias/{alias}")
    public ResponseEntity<Account> findByAlias(@PathVariable String alias, @RequestHeader("Authorization") String token) {
        return accountsRepository.findFirstByAlias(alias)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/findByCvu/{cvu}")
    public ResponseEntity<Account> findByCvu(@PathVariable String cvu,@RequestHeader("Authorization") String token) {
        return accountsRepository.findByCvu(cvu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountId}/activity")
    public ResponseEntity<?> getAllActivities(@PathVariable Long accountId, @RequestHeader("Authorization") String token) {
        try {
            // No se pasa el token, ya que la validación se realiza en el filtro
            List<ActivityOutDTO> activities = apiActivityFeignClient.getAllActivitiesByAccountId(accountId,"Bearer "+token);
            return ResponseEntity.ok(activities); // 200 OK
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(403).body("Sin permisos"); // 403 Forbidden
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("Cuenta no encontrada"); // 404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad request"); // 400 Bad Request
        }
    }

    @PostMapping("/{accountId}/cards")
    public ResponseEntity<?> createCard(
            @PathVariable Long accountId,
            @Valid @RequestBody CreateCardEntryDTO createCardEntryDTO,
            @RequestHeader("Authorization") String token) {
        try {
            // Validar token
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente");
            }

            String jwtToken = token.replace("Bearer ", "");

            // Llamar al cliente Feign
            ResponseEntity<?> newCard = apiCardFeignClient.createCard(accountId, createCardEntryDTO, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCard);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PostMapping("/{accountId}/transferences/cards")
    public ResponseEntity<String> registerTransference(
            @PathVariable Long accountId,
            @RequestBody TransferenceOutDTO transferenceOutDto, @RequestHeader("Authorization") String token) {

        try {
            apiTransferencesFeignClient.registerTransference(accountId, transferenceOutDto,token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso registrado con éxito");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta o tarjeta no encontrada");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permisos");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el ingreso");
        }
    }

    @PostMapping("/{accountId}/transferences/money")
    public ResponseEntity<?> makeTransfer(
            @PathVariable Long accountId,
            @RequestBody TransferRequestOutDTO transferRequest, @RequestHeader("Authorization") String token)throws AccountNotFoundException {
        try {
            // Realizar la transferencia
            apiTransferencesFeignClient.makeTransfer(accountId, transferRequest,token);
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

    @GetMapping("/{accountId}/activity/{activityId}/receipt")
    public ResponseEntity<byte[]> downloadActivityReceipt(@PathVariable Long accountId, @PathVariable Long activityId,@RequestHeader("Authorization") String token) {
        return apiActivityFeignClient.downloadActivityReceipt(accountId, activityId,token);
    }

}
