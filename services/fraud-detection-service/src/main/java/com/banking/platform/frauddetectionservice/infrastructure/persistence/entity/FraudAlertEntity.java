package com.banking.platform.frauddetectionservice.infrastructure.persistence.entity;
import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fraud_alerts", schema = "fraud")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class FraudAlertEntity {
    @Id @Column(columnDefinition = "uuid") private UUID id;
    @Column(name = "transaction_id", nullable = false) private UUID transactionId;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(nullable = false, precision = 19, scale = 4) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(name = "risk_level", nullable = false, length = 20) private FraudAlert.RiskLevel riskLevel;
    @Column(length = 1000) private String reason;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private FraudAlert.FraudStatus status;
    @Column(name = "detected_at", updatable = false) private Instant detectedAt;
}
