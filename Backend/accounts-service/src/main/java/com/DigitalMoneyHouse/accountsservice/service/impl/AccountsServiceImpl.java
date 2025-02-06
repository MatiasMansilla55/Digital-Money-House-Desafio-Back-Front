package com.DigitalMoneyHouse.accountsservice.service.impl;


import com.DigitalMoneyHouse.accountsservice.dto.entry.AccountEntryDTO;
import com.DigitalMoneyHouse.accountsservice.dto.exit.AccountOutDTO;

import com.DigitalMoneyHouse.accountsservice.exceptions.AccountPersistenceException;
import com.DigitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.DigitalMoneyHouse.accountsservice.repository.AccountsRepository;
import com.DigitalMoneyHouse.accountsservice.repository.TransactionRepository;
import com.DigitalMoneyHouse.accountsservice.service.IAccountService;
import com.DigitalMoneyHouse.accountsservice.dto.AccountCreationRequest;
import com.DigitalMoneyHouse.accountsservice.dto.AccountResponse;
import com.DigitalMoneyHouse.accountsservice.entities.Account;
import com.DigitalMoneyHouse.accountsservice.entities.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;
@Slf4j
@Service
public class AccountsServiceImpl implements IAccountService {
    private final Logger LOGGER = LoggerFactory.getLogger(AccountsServiceImpl.class);
    private final ModelMapper modelMapper;
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public AccountsServiceImpl(ModelMapper modelMapper, AccountsRepository accountsRepository, TransactionRepository transactionRepository) {
        this.modelMapper = modelMapper;
        this.accountsRepository=accountsRepository;
        this.transactionRepository= transactionRepository;
        configureMapping();
    }

    public AccountResponse getAccountSummary(Long accountId) throws ResourceNotFoundException {
        // Obtener la cuenta por ID
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // Crear respuesta con saldo disponible
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setBalance(account.getBalance());
        return response;
    }

    public List<Transaction> getLastTransactions(Long accountId) {
        // Obtener los últimos 5 movimientos
        return transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    public AccountOutDTO getAccountById(Long accountId) throws ResourceNotFoundException {
        // Obtener la cuenta de la base de datos
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        // Convertir la entidad Account a AccountDTO
        return modelMapper.map(account, AccountOutDTO.class);
    }

    public AccountOutDTO updateAccount(Long id, AccountEntryDTO accountEntryDTO) throws ResourceNotFoundException {
        // Verificar si la cuenta existe
        Account existingAccount = accountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        // Actualizar los campos de la cuenta existente
        modelMapper.map(accountEntryDTO, existingAccount);

        // Guardar la cuenta actualizada en la base de datos
        Account updatedAccount = accountsRepository.save(existingAccount);

        // Usar ModelMapper para convertir la entidad actualizada a AccountOutDTO
        AccountOutDTO accountOutDTO = modelMapper.map(updatedAccount, AccountOutDTO.class);

        return accountOutDTO; // Retornar el DTO
    }

    public  Account findByEmail(String email, String token) {
        return accountsRepository.findByEmail(email);
    }

    public List<AccountOutDTO> getAccounts() {
        List<Account> accounts = accountsRepository.findAll(); // Obtener todas las cuentas
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountOutDTO.class))// Convertir cada Account a AccountDTO
                .collect(Collectors.toList());
    }

    public AccountResponse createAccount(AccountCreationRequest request) {
        log.info("Iniciando creación de cuenta para User ID: {}", request.getUserId());
        log.debug("Datos de la solicitud: Email: {}, Alias: {}, CVU: {}, Saldo Inicial: {}",
                request.getEmail(), request.getAlias(), request.getCvu(), request.getInitialBalance());

        // Crear la entidad Account y mapear los datos
        Account account = new Account();
        account.setUserId(request.getUserId());
        account.setEmail(request.getEmail());
        account.setAlias(request.getAlias());
        account.setCvu(request.getCvu());
        account.setBalance(request.getInitialBalance());

        // Guardar la cuenta en la base de datos
        try {
            account = accountsRepository.save(account);
            log.info("Cuenta guardada exitosamente en la base de datos con ID: {},{},{},{}", account.getId(), account.getEmail(),account.getAlias(),account.getBalance());
        } catch (Exception e) {
            log.error("Error al guardar la cuenta en la base de datos: {}", e.getMessage(), e);
            throw new AccountPersistenceException("Error al registrar la cuenta");
        }

        // Crear y devolver la respuesta
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setBalance(account.getBalance());

        log.info("Cuenta creada con éxito. ID: {}, Balance: {}", response.getId(), response.getBalance());
        return response;
    }
    private void configureMapping() {
        modelMapper.typeMap(AccountOutDTO.class, Account.class);
        modelMapper.typeMap(Account.class, AccountOutDTO.class);
    }

}
