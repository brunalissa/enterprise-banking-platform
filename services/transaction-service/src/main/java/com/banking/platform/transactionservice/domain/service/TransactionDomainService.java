package com.banking.platform.transactionservice.domain.service;

import com.banking.platform.transactionservice.domain.exception.TransactionValidationException;
import com.banking.platform.transactionservice.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class TransactionDomainService {

    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("1000000");

    public void validate(Transaction transaction) {
        if (transaction.getCustomerId() == null) {
            throw new TransactionValidationException("Customer ID is required");
        }
        if (transaction.getSourceAccountId() == null) {
            throw new TransactionValidationException("Source account is required");
        }
        if (transaction.getTargetAccountId() == null) {
            throw new TransactionValidationException("Target account is required");
        }
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionValidationException("Amount must be positive");
        }
        if (transaction.getAmount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            throw new TransactionValidationException("Amount exceeds maximum limit of " + MAX_TRANSACTION_AMOUNT);
        }
        if (transaction.getSourceAccountId().equals(transaction.getTargetAccountId())) {
            throw new TransactionValidationException("Source and target accounts must be different");
        }
        log.debug("Transaction validated for customer: {}", transaction.getCustomerId());
    }
}
