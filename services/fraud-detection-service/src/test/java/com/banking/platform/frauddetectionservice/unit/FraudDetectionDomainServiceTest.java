package com.banking.platform.frauddetectionservice.unit;

import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import com.banking.platform.frauddetectionservice.domain.service.FraudDetectionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FraudDetectionDomainService Unit Tests")
class FraudDetectionDomainServiceTest {

    private FraudDetectionDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new FraudDetectionDomainService();
    }

    @Test
    @DisplayName("Should return LOW risk for normal transaction")
    void shouldReturnLowRiskForNormalTransaction() {
        FraudAlert.RiskLevel risk = domainService.evaluateRisk(
                UUID.randomUUID(), new BigDecimal("500"), 3, false, false);
        assertEquals(FraudAlert.RiskLevel.LOW, risk);
    }

    @Test
    @DisplayName("Should return CRITICAL risk for very high amount")
    void shouldReturnCriticalRiskForHighAmount() {
        FraudAlert.RiskLevel risk = domainService.evaluateRisk(
                UUID.randomUUID(), new BigDecimal("250000"), 3, false, false);
        assertEquals(FraudAlert.RiskLevel.CRITICAL, risk);
    }

    @Test
    @DisplayName("Should return HIGH risk for high amount")
    void shouldReturnHighRiskForHighAmount() {
        FraudAlert.RiskLevel risk = domainService.evaluateRisk(
                UUID.randomUUID(), new BigDecimal("60000"), 3, false, false);
        assertEquals(FraudAlert.RiskLevel.HIGH, risk);
    }

    @Test
    @DisplayName("Should return HIGH risk for high velocity")
    void shouldReturnHighRiskForHighVelocity() {
        FraudAlert.RiskLevel risk = domainService.evaluateRisk(
                UUID.randomUUID(), new BigDecimal("500"), 25, false, false);
        assertEquals(FraudAlert.RiskLevel.HIGH, risk);
    }

    @Test
    @DisplayName("Should return MEDIUM risk for new payee with significant amount")
    void shouldReturnMediumRiskForNewPayee() {
        FraudAlert.RiskLevel risk = domainService.evaluateRisk(
                UUID.randomUUID(), new BigDecimal("15000"), 3, true, false);
        assertEquals(FraudAlert.RiskLevel.MEDIUM, risk);
    }

    @Test
    @DisplayName("Should generate meaningful reason")
    void shouldGenerateMeaningfulReason() {
        String reason = domainService.generateReason(
                FraudAlert.RiskLevel.CRITICAL, new BigDecimal("250000"), 25, true, true);
        assertNotNull(reason);
        assertTrue(reason.contains("Critical"));
        assertTrue(reason.contains("Velocity"));
    }
}
