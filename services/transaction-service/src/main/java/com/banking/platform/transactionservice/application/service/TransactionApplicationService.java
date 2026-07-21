package com.banking.platform.transactionservice.application.service;

import com.banking.platform.transactionservice.application.dto.*;
import com.banking.platform.transactionservice.domain.exception.TransactionNotFoundException;
import com.banking.platform.transactionservice.domain.model.*;
import com.banking.platform.transactionservice.domain.repository.*;
import com.banking.platform.transactionservice.domain.service.TransactionDomainService;
import com.banking.platform.transactionservice.infrastructure.messaging.TransactionEventPublisher;
import com.banking.platform.transactionservice.infrastructure.messaging.event.TransactionCompletedEvent;
import com.banking.platform.transactionservice.infrastructure.persistence.entity.IdempotencyKeyEntity;
import com.banking.platform.transactionservice.infrastructure.persistence.repository.IdempotencyKeyJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionApplicationService {

    private final TransactionRepository transactionRepository;
    private final OutboxRepository outboxRepository;
    private final IdempotencyKeyJpaRepository idempotencyKeyRepository;
    private final TransactionDomainService domainService;
    private final TransactionEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        // Idempotency check
        if (request.getIdempotencyKey() != null) {
            var existing = idempotencyKeyRepository.findById(request.getIdempotencyKey());
            if (existing.isPresent()) {
                log.info("Idempotent request detected, returning existing transaction: {}", existing.get().getTransactionId());
                return getTransaction(existing.get().getTransactionId());
            }
        }

        log.info("Processing transfer of {} {} from account {} to {}",
                request.getAmount(), request.getCurrency(), request.getSourceAccountId(), request.getTargetAccountId());

        UUID txId = UUID.randomUUID();
        String reference = "TXN-" + txId.toString().substring(0, 8).toUpperCase();

        Transaction transaction = Transaction.builder()
                .id(txId)
                .customerId(request.getCustomerId())
                .sourceAccountId(request.getSourceAccountId())
                .targetAccountId(request.getTargetAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.PENDING)
                .reference(reference)
                .description(request.getDescription())
                .createdAt(Instant.now())
                .build();

        domainService.validate(transaction);

        // Update to PROCESSING
        transaction = Transaction.toBuilder(transaction)
                .status(Transaction.TransactionStatus.PROCESSING)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        // Save idempotency key
        if (request.getIdempotencyKey() != null) {
            idempotencyKeyRepository.save(IdempotencyKeyEntity.builder()
                    .key(request.getIdempotencyKey())
                    .transactionId(saved.getId())
                    .build());
        }

        // Write to outbox (Outbox Pattern)
        try {
            String payload = objectMapper.writeValueAsString(TransactionCompletedEvent.builder()
                    .transactionId(saved.getId())
                    .customerId(saved.getCustomerId())
                    .sourceAccountId(saved.getSourceAccountId())
                    .targetAccountId(saved.getTargetAccountId())
                    .amount(saved.getAmount())
                    .currency(saved.getCurrency())
                    .reference(saved.getReference())
                    .timestamp(Instant.now())
                    .build());

            outboxRepository.save(OutboxMessage.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("Transaction")
                    .aggregateId(saved.getId())
                    .eventType("TransactionCompleted")
                    .payload(payload)
                    .status(OutboxMessage.OutboxStatus.PENDING)
                    .createdAt(Instant.now())
                    .retryCount(0)
                    .build());

            // Mark as completed (in production, the outbox processor would do this asynchronously)
            saved = Transaction.toBuilder(saved)
                    .status(Transaction.TransactionStatus.COMPLETED)
                    .completedAt(Instant.now())
                    .build();
            saved = transactionRepository.save(saved);

            // Publish event directly (for real-time notification)
            eventPublisher.publishTransactionCompleted(saved);

        } catch (Exception e) {
            log.error("Failed to create outbox message", e);
            saved = Transaction.toBuilder(saved)
                    .status(Transaction.TransactionStatus.FAILED)
                    .completedAt(Instant.now())
                    .build();
            saved = transactionRepository.save(saved);
        }

        log.info("Transfer completed: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByCustomer(UUID customerId) {
        return transactionRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findBySourceAccountId(accountId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .customerId(t.getCustomerId())
                .sourceAccountId(t.getSourceAccountId())
                .targetAccountId(t.getTargetAccountId())
                .amount(t.getAmount())
                .currency(t.getCurrency())
                .type(t.getType())
                .status(t.getStatus())
                .reference(t.getReference())
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .completedAt(t.getCompletedAt())
                .build();
    }
}
