package com.banking.platform.customerservice.infrastructure.messaging.event;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreatedEvent {
    private UUID eventId;
    private UUID customerId;
    private String email;
    private String fullName;
    private Instant timestamp;
}