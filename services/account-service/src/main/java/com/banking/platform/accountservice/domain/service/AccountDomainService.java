package com.banking.platform.accountservice.domain.service;

import com.banking.platform.accountservice.domain.exception.InsufficientFundsException;
import com.banking.platform.accountservice.domain.model.Account;
import com.banking.platform.accountservice.domain.model.AccountTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class AccountDomainService {

    public AccountTransaction deposit(Account account, BigDecimal amount, String reference) {
        if (account.isFrozen()) {
            log.warn("Attempted deposit to frozen account: {}", account.getId());
            throw new IllegalStateException("Account is frozen");
        }

        BigDecimal newBalance = account.getBalance().add(amount);

        return AccountTransaction.builder()
                .id(UUID.randomUUID())
                .accountId(account.getId())
                .type(AccountTransaction.TransactionType.CREDIT)
                .amount(amount)
                .currency(account.getCurrency())
                .reference(reference)
                .balanceAfter(newBalance)
                .createdAt(java.time.Instant.now())
                .build();
    }

    public AccountTransaction withdraw(Account account, BigDecimal amount, String reference) {
        if (account.isFrozen()) {
            throw new IllegalStateException("Account is frozen");
        }
        if (!account.canWithdraw(amount)) {
            throw new InsufficientFundsException(account.getId(), amount, account.getBalance());
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);

        return AccountTransaction.builder()
                .id(UUID.randomUUID())
                .accountId(account.getId())
                .type(AccountTransaction.TransactionType.DEBIT)
                .amount(amount)
                .currency(account.getCurrency())
                .reference(reference)
                .balanceAfter(newBalance)
                .createdAt(java.time.Instant.now())
                .build();
    }

    public void validateNewAccount(Account account) {
        if (account.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (account.getCurrency() == null || account.getCurrency().isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        log.debug("Account validation passed for customer: {}", account.getCustomerId());
    }
}
