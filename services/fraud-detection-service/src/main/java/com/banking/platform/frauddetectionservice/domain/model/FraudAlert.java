package com.banking.platform.frauddetectionservice.domain.model;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class FraudAlert {
    private UUID id;
    private UUID transactionId;
    private UUID customerId;
    private BigDecimal amount;
    private RiskLevel riskLevel;
    private String reason;
    private FraudStatus status;
    private Instant detectedAt;

    public enum RiskLevel { LOW, MEDIUM, HIGH, CRITICAL }
    public enum FraudStatus { OPEN, INVESTIGATING, CONFIRMED_FRAUD, FALSE_POSITIVE }
}
