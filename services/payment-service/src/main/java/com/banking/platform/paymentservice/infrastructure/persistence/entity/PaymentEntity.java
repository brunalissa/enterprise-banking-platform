package com.banking.platform.paymentservice.infrastructure.persistence.entity;
import com.banking.platform.paymentservice.domain.model.Payment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments", schema = "payment")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class PaymentEntity {
    @Id @Column(columnDefinition = "uuid") private UUID id;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(name = "account_id", nullable = false) private UUID accountId;
    @Column(nullable = false, length = 255) private String payee;
    @Column(name = "payee_account", nullable = false, length = 30) private String payeeAccount;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(length = 30) private Payment.PaymentType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Payment.PaymentStatus status;
    @Column(length = 100) private String reference;
    @Column(name = "idempotency_key", unique = true, length = 100) private String idempotencyKey;
    @CreationTimestamp @Column(name = "created_at", updatable = false) private Instant createdAt;
    @Column(name = "confirmed_at") private Instant confirmedAt;
}
