package com.banking.platform.transactionservice.infrastructure.messaging.event;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCompletedEvent {
    private UUID eventId;
    private UUID transactionId;
    private UUID customerId;
    private UUID sourceAccountId;
    private UUID targetAccountId;
    private BigDecimal amount;
    private String currency;
    private String reference;
    private Instant timestamp;
}