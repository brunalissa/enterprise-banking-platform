package com.banking.platform.transactionservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboxMessage {
    private UUID id;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    private String payload;
    private OutboxStatus status;
    private Instant createdAt;
    private Instant processedAt;
    private int retryCount;

    public enum OutboxStatus {
        PENDING, PROCESSED, FAILED
    }
}