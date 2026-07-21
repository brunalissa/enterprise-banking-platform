package com.banking.platform.paymentservice.infrastructure.messaging.event;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmedEvent {
    private UUID eventId;
    private UUID paymentId;
    private UUID customerId;
    private BigDecimal amount;
    private String currency;
    private String reference;
    private Instant timestamp;
}