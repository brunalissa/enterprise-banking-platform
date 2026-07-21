package com.banking.platform.transactionservice.domain.repository;

import com.banking.platform.transactionservice.domain.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByCustomerId(UUID customerId);
    List<Transaction> findBySourceAccountId(UUID accountId);
}
