package com.banking.platform.transactionservice.application.dto;

import com.banking.platform.transactionservice.domain.model.Transaction;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private UUID id;
    private UUID customerId;
    private UUID sourceAccountId;
    private UUID targetAccountId;
    private BigDecimal amount;
    private String currency;
    private Transaction.TransactionType type;
    private Transaction.TransactionStatus status;
    private String reference;
    private String description;
    private Instant createdAt;
    private Instant completedAt;
}
