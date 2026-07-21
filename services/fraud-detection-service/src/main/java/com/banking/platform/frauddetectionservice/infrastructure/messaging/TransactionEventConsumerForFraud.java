package com.banking.platform.frauddetectionservice.infrastructure.messaging;

import com.banking.platform.frauddetectionservice.application.service.FraudDetectionApplicationService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventConsumerForFraud {

    private final FraudDetectionApplicationService fraudService;

    @KafkaListener(topics = "transaction-events", groupId = "fraud-detection-service")
    public void consumeTransactionEvent(JsonNode event) {
        try {
            UUID transactionId = UUID.fromString(event.get("transactionId").asText());
            UUID customerId = UUID.fromString(event.get("customerId").asText());
            BigDecimal amount = new BigDecimal(event.get("amount").asText());

            // In production, these would come from real data
            int recentTxCount = 5; // placeholder
            boolean isNewPayee = false;
            boolean isUnusualHours = false;

            fraudService.analyzeTransaction(transactionId, customerId, amount, recentTxCount, isNewPayee, isUnusualHours);
            log.info("Analyzed transaction for fraud: {}", transactionId);
        } catch (Exception e) {
            log.error("Failed to analyze transaction for fraud", e);
        }
    }
}
