package com.banking.platform.frauddetectionservice.domain.service;

import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fraud Detection Rules Engine.
 * 
 * Evaluates transactions against multiple risk indicators:
 * 1. Amount threshold rules
 * 2. Velocity rules (frequency of transactions)
 * 3. Time-based rules (unusual hours)
 * 4. New payee risk
 */
@Slf4j
@Service
public class FraudDetectionDomainService {

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("50000");
    private static final BigDecimal CRITICAL_AMOUNT_THRESHOLD = new BigDecimal("200000");
    private static final int MAX_TRANSACTIONS_PER_HOUR = 20;

    public FraudAlert.RiskLevel evaluateRisk(UUID customerId, BigDecimal amount,
                                              int recentTransactionCount, boolean isNewPayee,
                                              boolean isUnusualHours) {
        log.debug("Evaluating risk for customer: {} amount: {}", customerId, amount);

        List<FraudAlert.RiskLevel> triggered = new ArrayList<>();

        // Rule 1: High amount threshold
        if (amount.compareTo(CRITICAL_AMOUNT_THRESHOLD) >= 0) {
            triggered.add(FraudAlert.RiskLevel.CRITICAL);
        } else if (amount.compareTo(HIGH_AMOUNT_THRESHOLD) >= 0) {
            triggered.add(FraudAlert.RiskLevel.HIGH);
        }

        // Rule 2: Velocity check
        if (recentTransactionCount > MAX_TRANSACTIONS_PER_HOUR) {
            triggered.add(FraudAlert.RiskLevel.HIGH);
        } else if (recentTransactionCount > 10) {
            triggered.add(FraudAlert.RiskLevel.MEDIUM);
        }

        // Rule 3: New payee risk
        if (isNewPayee && amount.compareTo(new BigDecimal("10000")) > 0) {
            triggered.add(FraudAlert.RiskLevel.MEDIUM);
        }

        // Rule 4: Unusual hours
        if (isUnusualHours && amount.compareTo(new BigDecimal("5000")) > 0) {
            triggered.add(FraudAlert.RiskLevel.MEDIUM);
        }

        // Return highest risk level
        if (triggered.contains(FraudAlert.RiskLevel.CRITICAL)) return FraudAlert.RiskLevel.CRITICAL;
        if (triggered.contains(FraudAlert.RiskLevel.HIGH)) return FraudAlert.RiskLevel.HIGH;
        if (triggered.contains(FraudAlert.RiskLevel.MEDIUM)) return FraudAlert.RiskLevel.MEDIUM;
        return FraudAlert.RiskLevel.LOW;
    }

    public String generateReason(FraudAlert.RiskLevel level, BigDecimal amount,
                                  int txCount, boolean isNewPayee, boolean unusualHours) {
        StringBuilder sb = new StringBuilder();
        if (level == FraudAlert.RiskLevel.CRITICAL) sb.append("Critical: amount exceeds ").append(CRITICAL_AMOUNT_THRESHOLD).append(". ");
        if (level == FraudAlert.RiskLevel.HIGH && amount.compareTo(HIGH_AMOUNT_THRESHOLD) >= 0) sb.append("High amount: ").append(amount).append(". ");
        if (txCount > MAX_TRANSACTIONS_PER_HOUR) sb.append("Velocity breach: ").append(txCount).append(" transactions. ");
        if (isNewPayee) sb.append("New payee. ");
        if (unusualHours) sb.append("Unusual time. ");
        return sb.toString().trim();
    }
}
