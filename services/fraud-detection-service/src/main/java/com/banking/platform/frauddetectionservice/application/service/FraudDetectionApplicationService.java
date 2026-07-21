package com.banking.platform.frauddetectionservice.application.service;

import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import com.banking.platform.frauddetectionservice.domain.repository.FraudAlertRepository;
import com.banking.platform.frauddetectionservice.domain.service.FraudDetectionDomainService;
import com.banking.platform.frauddetectionservice.infrastructure.messaging.FraudEventPublisher;
import com.banking.platform.frauddetectionservice.infrastructure.messaging.event.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudDetectionApplicationService {

    private final FraudAlertRepository fraudAlertRepository;
    private final FraudDetectionDomainService domainService;
    private final FraudEventPublisher eventPublisher;

    @Transactional
    public FraudAlert analyzeTransaction(UUID transactionId, UUID customerId, BigDecimal amount,
                                          int recentTransactionCount, boolean isNewPayee, boolean isUnusualHours) {
        log.info("Analyzing transaction {} for customer {} - amount: {}", transactionId, customerId, amount);

        FraudAlert.RiskLevel riskLevel = domainService.evaluateRisk(
                customerId, amount, recentTransactionCount, isNewPayee, isUnusualHours);

        String reason = domainService.generateReason(riskLevel, amount, recentTransactionCount, isNewPayee, isUnusualHours);

        FraudAlert alert = FraudAlert.builder()
                .id(UUID.randomUUID())
                .transactionId(transactionId)
                .customerId(customerId)
                .amount(amount)
                .riskLevel(riskLevel)
                .reason(reason.isEmpty() ? "No risk indicators triggered" : reason)
                .status(FraudAlert.FraudStatus.OPEN)
                .detectedAt(Instant.now())
                .build();

        alert = fraudAlertRepository.save(alert);

        // Publish event if medium risk or above
        if (riskLevel != FraudAlert.RiskLevel.LOW) {
            eventPublisher.publishFraudAlert(FraudAlertEvent.builder()
                    .alertId(alert.getId())
                    .customerId(customerId)
                    .transactionId(transactionId)
                    .riskLevel(riskLevel.name())
                    .reason(reason)
                    .build());
        }

        log.info("Fraud analysis completed - risk level: {} for transaction: {}", riskLevel, transactionId);
        return alert;
    }

    @Transactional(readOnly = true)
    public List<FraudAlert> getAlertsByCustomer(UUID customerId) {
        return fraudAlertRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<FraudAlert> getOpenAlerts() {
        return fraudAlertRepository.findByStatus(FraudAlert.FraudStatus.OPEN);
    }

    @Transactional
    public FraudAlert updateAlertStatus(UUID alertId, FraudAlert.FraudStatus status) {
        FraudAlert alert = fraudAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Fraud alert not found: " + alertId));

        FraudAlert updated = FraudAlert.builder()
                .id(alert.getId())
                .transactionId(alert.getTransactionId())
                .customerId(alert.getCustomerId())
                .amount(alert.getAmount())
                .riskLevel(alert.getRiskLevel())
                .reason(alert.getReason())
                .status(status)
                .detectedAt(alert.getDetectedAt())
                .build();

        return fraudAlertRepository.save(updated);
    }
}
