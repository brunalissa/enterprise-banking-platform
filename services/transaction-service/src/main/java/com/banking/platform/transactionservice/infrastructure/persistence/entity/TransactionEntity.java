package com.banking.platform.transactionservice.infrastructure.persistence.entity;

import com.banking.platform.transactionservice.domain.model.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions", schema = "transaction")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class TransactionEntity {
    @Id @Column(columnDefinition = "uuid") private UUID id;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(name = "source_account_id", nullable = false) private UUID sourceAccountId;
    @Column(name = "target_account_id", nullable = false) private UUID targetAccountId;
    @Column(name = "source_account_number") private String sourceAccountNumber;
    @Column(name = "target_account_number") private String targetAccountNumber;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(length = 20) private Transaction.TransactionType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Transaction.TransactionStatus status;
    @Column(length = 100) private String reference;
    @Column(length = 500) private String description;
    @CreationTimestamp @Column(name = "created_at", updatable = false) private Instant createdAt;
    @Column(name = "completed_at") private Instant completedAt;
}
