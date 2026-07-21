package com.banking.platform.accountservice.infrastructure.persistence.entity;

import com.banking.platform.accountservice.domain.model.AccountTransaction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_transactions", schema = "account")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class AccountTransactionEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "account_id", nullable = false, columnDefinition = "uuid")
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountTransaction.TransactionType type;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(length = 255)
    private String reference;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceAfter;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
