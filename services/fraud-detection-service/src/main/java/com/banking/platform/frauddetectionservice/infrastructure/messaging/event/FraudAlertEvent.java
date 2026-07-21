package com.banking.platform.frauddetectionservice.infrastructure.messaging.event;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudAlertEvent {
    private UUID eventId;
    private UUID alertId;
    private UUID customerId;
    private UUID transactionId;
    private String riskLevel;
    private String reason;
    private Instant timestamp;
}