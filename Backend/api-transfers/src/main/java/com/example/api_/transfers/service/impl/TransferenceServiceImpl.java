package com.example.api_.transfers.service.impl;

import com.example.api_.transfers.dto.entry.Account;
import com.example.api_.transfers.dto.entry.Activity;
import com.example.api_.transfers.dto.entry.Card;
import com.example.api_.transfers.dto.exit.TransferRequestOutDTO;
import com.example.api_.transfers.dto.exit.TransferenceOutDTO;
import com.example.api_.transfers.entities.Transference;
import com.example.api_.transfers.exceptions.InsufficientFundsException;
import com.example.api_.transfers.exceptions.ResourceNotFoundException;
import com.example.api_.transfers.exceptions.UnauthorizedException;
import com.example.api_.transfers.feign.AccountFeignClient;
import com.example.api_.transfers.feign.ActivityFeignClient;
import com.example.api_.transfers.feign.CardFeignClient;
import com.example.api_.transfers.repository.TransferenceRepository;

import com.example.api_.transfers.service.ITransferenceService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferenceServiceImpl implements ITransferenceService {
    private final Logger LOGGER = LoggerFactory.getLogger(TransferenceServiceImpl.class);
    @Autowired
    private TransferenceRepository transferenceRepository;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private CardFeignClient cardFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    public TransferenceServiceImpl(TransferenceRepository transferenceRepository, AccountFeignClient accountFeignClient, CardFeignClient cardFeignClient, ActivityFeignClient activityFeignClient) {
        this.transferenceRepository = transferenceRepository;
        this.accountFeignClient = accountFeignClient;
        this.cardFeignClient= cardFeignClient;
        this.activityFeignClient = activityFeignClient;
    }

    public void registerTransferenceFromCards(Long accountId, TransferenceOutDTO transferenceOutDto, String token) throws ResourceNotFoundException, UnauthorizedException {

        // Validar que la cuenta existe
        Account account = Optional.ofNullable(accountFeignClient.getAccountById(accountId, token))
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));


        // Validar que la tarjeta existe
        Card card = cardFeignClient.getCardtById(accountId,transferenceOutDto.getCardId(),token)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        // Crear la transferencia
        Transference transference = new Transference();
        transference.setAccountId(account.getId());
        transference.setCardId(card.getId());
        transference.setAmount(transferenceOutDto.getAmount());
        transference.setDate(LocalDateTime.now());
        transference.setType("deposit"); // Para depósitos
        transference.setRecipient(account.getCvu());

        // Guardar la transferencia
        transferenceRepository.save(transference);

        // Actualizar el balance de la cuenta usando BigDecimal
        BigDecimal currentBalance = account.getBalance();
        BigDecimal amount = transferenceOutDto.getAmount();

        // Sumamos el monto al balance actual
        BigDecimal newBalance = currentBalance.add(amount);

        // Actualizamos el balance en la cuenta
        account.setBalance(newBalance);

        // Guardar la cuenta con el nuevo balance
        accountFeignClient.saveAccount( account,accountId, token);
        LOGGER.info("Cuenta enviada a account-service con balance: " + account.getBalance());
        LOGGER.info("Actualizando cuenta con ID: {}, Balance actual: {}, Nuevo balance: {}",
                account.getBalance(), account.getBalance().add(amount));
        // Registrar la actividad
        Activity activity = new Activity();
        activity.setAccountId(accountId);
        activity.setType("deposit"); // Tipo de actividad
        activity.setAmount(amount); // Monto de la transferencia
        activity.setDescription("Depósito de " + amount + " realizado con la tarjeta " + card.getNumber()); // Descripción
        activity.setDate(LocalDateTime.now()); // Fecha de la actividad

        activityFeignClient.save(activity,accountId,token);
    }

    @Transactional
    public void makeTransferFromCash(Long accountId, TransferRequestOutDTO transferRequest, String token) throws AccountNotFoundException {
        Logger logger = LoggerFactory.getLogger(TransferenceServiceImpl.class);

        logger.info("Iniciando transferencia desde cuenta con ID: {}", accountId);

        try {
            // Validar la existencia de la cuenta que envía el dinero
            logger.info("Obteniendo cuenta remitente por ID: {}", accountId);
            Account senderAccount = Optional.ofNullable(accountFeignClient.getAccountById(accountId, token))
                    .orElseThrow(() -> new AccountNotFoundException("Cuenta remitente inexistente"));
            logger.info("Cuenta remitente obtenida: {}", senderAccount);

            // Validar fondos
            logger.info("Validando fondos de la cuenta remitente. Saldo actual: {}, Monto solicitado: {}",
                    senderAccount.getBalance(), transferRequest.getAmount());
            if (senderAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
                throw new InsufficientFundsException("Fondos insuficientes");
            }

            // Buscar la cuenta del destinatario por alias o CVU
            logger.info("Buscando cuenta destinataria con identificador: {}", transferRequest.getRecipient());
            Account recipientAccount = findRecipientAccount(transferRequest.getRecipient(), token);
            logger.info("Cuenta destinataria obtenida: {}", recipientAccount);

            // Actualizar balances
            logger.info("Actualizando balances...");
            senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequest.getAmount())); // Resta del saldo del remitente
            recipientAccount.setBalance(recipientAccount.getBalance().add(transferRequest.getAmount())); // Suma al saldo del destinatario
            logger.info("Nuevos saldos: Remitente: {}, Destinatario: {}",
                    senderAccount.getBalance(), recipientAccount.getBalance());

            // Guardar los cambios en ambas cuentas
            logger.info("Guardando cuentas actualizadas...");
            accountFeignClient.saveAccount(senderAccount,accountId, token);
            accountFeignClient.saveAccount(recipientAccount,accountId, token);
            logger.info("Cuentas actualizadas correctamente.");

            // Guardar la transferencia en la base de datos
            logger.info("Registrando transferencia...");
            Transference transfer = new Transference();
            transfer.setAccountId(accountId); // Cuenta que envía
            transfer.setAmount(transferRequest.getAmount());
            transfer.setType("transfer-out"); // Para transferencias enviadas
            transfer.setRecipient(transferRequest.getRecipient());
            transferenceRepository.save(transfer);
            logger.info("Transferencia registrada: {}", transfer);

            // Registrar la actividad para la cuenta que envía
            logger.info("Registrando actividad para la cuenta remitente...");
            Activity senderActivity = new Activity();
            senderActivity.setAccountId(accountId);
            senderActivity.setType("transfer-out"); // Tipo de actividad
            senderActivity.setAmount(transferRequest.getAmount().negate()); // Monto de la transferencia (negado)
            senderActivity.setDescription(transferRequest.getRecipient()); // Descripción
            senderActivity.setDate(LocalDateTime.now());
            activityFeignClient.save(senderActivity,accountId, token);
            logger.info("Actividad para remitente registrada: {}", senderActivity);

            // Registrar la actividad para la cuenta que recibe
            logger.info("Registrando actividad para la cuenta destinataria...");
            Activity recipientActivity = new Activity();
            recipientActivity.setAccountId(recipientAccount.getUserId());
            recipientActivity.setType("transfer-in"); // Tipo de actividad
            recipientActivity.setAmount(transferRequest.getAmount()); // Monto de la transferencia
            recipientActivity.setDescription(senderAccount.getCvu()); // Descripción
            recipientActivity.setDate(LocalDateTime.now()); // Fecha de la actividad
            activityFeignClient.save(recipientActivity,accountId, token);
            logger.info("Actividad para destinatario registrada: {}", recipientActivity);

        } catch (AccountNotFoundException e) {
            logger.error("Error: Cuenta no encontrada", e);
            throw e;
        } catch (InsufficientFundsException e) {
            logger.error("Error: Fondos insuficientes", e);
            throw e;
        } catch (UnauthorizedException e) {
            logger.error("Error: Permisos insuficientes", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado durante la transferencia", e);
            throw e;
        }
    }


    // Método para encontrar la cuenta del destinatario
    public Account findRecipientAccount(String recipientIdentifier, String token) throws AccountNotFoundException {
        Account recipientAccount;

        // Intenta encontrar la cuenta por alias
        try {
            recipientAccount = accountFeignClient.findByAlias(recipientIdentifier,token);
            if (recipientAccount != null) {
                return recipientAccount;
            }
        } catch (FeignException.NotFound e) {
            System.out.println("No se encontró la cuenta por alias, intentando por CVU. Identificador: " + recipientIdentifier);
        } catch (FeignException e) {
            System.err.println("Error inesperado al buscar por alias: " + e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de cuentas", e);
        }

        // Si no se encuentra por alias, intenta encontrar por CVU
        try {
            recipientAccount = accountFeignClient.findByCvu(recipientIdentifier, token);
            if (recipientAccount != null) {
                return recipientAccount;
            }
        } catch (FeignException.NotFound e) {
            System.out.println("No se encontró la cuenta por CVU. Identificador: " + recipientIdentifier);
        } catch (FeignException e) {
            System.err.println("Error inesperado al buscar por CVU: " + e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de cuentas", e);
        }

        // Si no se encuentra por alias ni por CVU, lanzar excepción
        throw new AccountNotFoundException("Cuenta destinataria inexistente para identificador: " + recipientIdentifier);
    }



    // Método para obtener las últimas 5 cuentas a las que se transfirió dinero
    public List<Transference> getLastTransferredAccounts(Long accountId) {
        return transferenceRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }


}