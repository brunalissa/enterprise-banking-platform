package com.banking.platform.accountservice.application.service;

import com.banking.platform.accountservice.application.dto.*;
import com.banking.platform.accountservice.domain.exception.AccountNotFoundException;
import com.banking.platform.accountservice.domain.model.*;
import com.banking.platform.accountservice.domain.repository.*;
import com.banking.platform.accountservice.domain.service.AccountDomainService;
import com.banking.platform.accountservice.infrastructure.messaging.AccountEventPublisher;
import com.banking.platform.accountservice.infrastructure.messaging.event.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountApplicationService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository transactionRepository;
    private final AccountDomainService domainService;
    private final AccountEventPublisher eventPublisher;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for customer: {}", request.getCustomerId());

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(request.getCustomerId())
                .accountNumber(accountNumber)
                .type(request.getType())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency())
                .status(Account.AccountStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        domainService.validateNewAccount(account);
        Account saved = accountRepository.save(account);

        // Record initial deposit if balance > 0
        if (request.getInitialBalance().compareTo(BigDecimal.ZERO) > 0) {
            AccountTransaction tx = domainService.deposit(saved, request.getInitialBalance(), "Initial deposit");
            transactionRepository.save(tx);
        }

        eventPublisher.publishAccountCreated(AccountCreatedEvent.builder()
                .accountId(saved.getId())
                .customerId(saved.getCustomerId())
                .accountNumber(saved.getAccountNumber())
                .build());

        log.info("Account created: {}", saved.getAccountNumber());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID id) {
        return accountRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(this::toResponse)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByCustomer(UUID customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountHistory(UUID accountId, int page, int size) {
        return transactionRepository.findByAccountId(accountId, page, size).stream()
                .map(this::toTxResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse freezeAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        Account frozen = Account.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(Account.AccountStatus.FROZEN)
                .createdAt(account.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        Account saved = accountRepository.save(frozen);
        log.info("Account frozen: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public AccountResponse closeAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance");
        }

        Account closed = Account.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(Account.AccountStatus.CLOSED)
                .createdAt(account.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        Account saved = accountRepository.save(closed);
        log.info("Account closed: {}", saved.getId());
        return toResponse(saved);
    }

    private String generateAccountNumber() {
        return "BR" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private AccountResponse toResponse(Account a) {
        return AccountResponse.builder()
                .id(a.getId())
                .customerId(a.getCustomerId())
                .accountNumber(a.getAccountNumber())
                .type(a.getType())
                .balance(a.getBalance())
                .currency(a.getCurrency())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    private TransactionResponse toTxResponse(AccountTransaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .accountId(t.getAccountId())
                .type(t.getType())
                .amount(t.getAmount())
                .currency(t.getCurrency())
                .reference(t.getReference())
                .balanceAfter(t.getBalanceAfter())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
