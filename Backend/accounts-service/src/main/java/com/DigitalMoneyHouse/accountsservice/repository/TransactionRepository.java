package com.DigitalMoneyHouse.accountsservice.repository;

import com.DigitalMoneyHouse.accountsservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByAccountIdOrderByDateDesc(Long accountId);
}
