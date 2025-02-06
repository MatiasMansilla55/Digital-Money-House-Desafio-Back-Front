package com.example.api_.transfers.service;

import com.example.api_.transfers.dto.entry.Account;
import com.example.api_.transfers.dto.exit.TransferRequestOutDTO;
import com.example.api_.transfers.dto.exit.TransferenceOutDTO;
import com.example.api_.transfers.entities.Transference;
import com.example.api_.transfers.exceptions.ResourceNotFoundException;
import com.example.api_.transfers.exceptions.UnauthorizedException;


import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface ITransferenceService {
    void registerTransferenceFromCards(Long accountId, TransferenceOutDTO transferenceOutDto, String token) throws ResourceNotFoundException, UnauthorizedException;
    void makeTransferFromCash(Long accountId, TransferRequestOutDTO transferRequest,String token) throws AccountNotFoundException;
    Account findRecipientAccount(String recipientIdentifier,String token) throws AccountNotFoundException;
    List<Transference> getLastTransferredAccounts(Long accountId);
}
