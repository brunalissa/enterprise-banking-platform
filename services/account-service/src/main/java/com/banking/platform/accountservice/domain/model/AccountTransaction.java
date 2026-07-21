package com.banking.platform.accountservice.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransaction {
    private UUID id;
    private UUID accountId;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private String reference;
    private BigDecimal balanceAfter;
    private Instant createdAt;

    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER_IN, TRANSFER_OUT, FEE, INTEREST
    }
}
