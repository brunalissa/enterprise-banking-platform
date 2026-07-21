package com.banking.platform.transactionservice.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private UUID id;
    private UUID customerId;
    private UUID sourceAccountId;
    private UUID targetAccountId;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private TransactionStatus status;
    private String reference;
    private String description;
    private Instant createdAt;
    private Instant completedAt;

    public enum TransactionType {
        TRANSFER, DEPOSIT, WITHDRAWAL, PAYMENT
    }

    public enum TransactionStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, COMPENSATED
    }

    public boolean isTerminal() {
        return this.status == TransactionStatus.COMPLETED || this.status == TransactionStatus.FAILED || this.status == TransactionStatus.COMPENSATED;
    }

    public static TransactionBuilder toBuilder(Transaction tx) {
        return Transaction.builder()
                .id(tx.getId())
                .customerId(tx.getCustomerId())
                .sourceAccountId(tx.getSourceAccountId())
                .targetAccountId(tx.getTargetAccountId())
                .sourceAccountNumber(tx.getSourceAccountNumber())
                .targetAccountNumber(tx.getTargetAccountNumber())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .type(tx.getType())
                .status(tx.getStatus())
                .reference(tx.getReference())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt())
                .completedAt(tx.getCompletedAt());
    }
}
