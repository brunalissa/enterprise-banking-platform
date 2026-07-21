package com.banking.platform.frauddetectionservice.domain.model;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class FraudRule {
    private String name;
    private String description;
    private FraudAlert.RiskLevel riskLevelIfTriggered;

    public boolean evaluate(UUID customerId, BigDecimal amount, int recentTransactionCount, boolean isNewPayee) {
        return false; /* overridden in specific rules */
    }
}
