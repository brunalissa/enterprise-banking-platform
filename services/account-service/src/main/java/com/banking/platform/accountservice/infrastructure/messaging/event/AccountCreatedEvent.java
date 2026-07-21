package com.banking.platform.accountservice.infrastructure.messaging.event;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {
    private UUID eventId;
    private UUID accountId;
    private UUID customerId;
    private String accountNumber;
    private Instant timestamp;
}