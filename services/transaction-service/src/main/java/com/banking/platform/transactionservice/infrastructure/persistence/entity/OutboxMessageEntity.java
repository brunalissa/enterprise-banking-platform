package com.banking.platform.transactionservice.infrastructure.persistence.entity;

import com.banking.platform.transactionservice.domain.model.OutboxMessage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages", schema = "transaction")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class OutboxMessageEntity {
    @Id @Column(columnDefinition = "uuid") private UUID id;
    @Column(name = "aggregate_type", nullable = false, length = 50) private String aggregateType;
    @Column(name = "aggregate_id", nullable = false) private UUID aggregateId;
    @Column(name = "event_type", nullable = false, length = 100) private String eventType;
    @Column(nullable = false, length = 4000) private String payload;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private OutboxMessage.OutboxStatus status;
    @CreationTimestamp @Column(name = "created_at", updatable = false) private Instant createdAt;
    @Column(name = "processed_at") private Instant processedAt;
    @Column(name = "retry_count") private int retryCount;
}
