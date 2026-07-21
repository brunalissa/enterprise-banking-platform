package com.banking.platform.transactionservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "idempotency_keys", schema = "transaction")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class IdempotencyKeyEntity {
    @Id
    @Column(length = 100)
    private String key;

    @Column(name = "transaction_id", columnDefinition = "uuid")
    private UUID transactionId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
