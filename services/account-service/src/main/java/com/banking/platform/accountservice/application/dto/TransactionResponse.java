package com.banking.platform.accountservice.application.dto;

import com.banking.platform.accountservice.domain.model.AccountTransaction;
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
    private UUID accountId;
    private AccountTransaction.TransactionType type;
    private BigDecimal amount;
    private String currency;
    private String reference;
    private BigDecimal balanceAfter;
    private Instant createdAt;
}
