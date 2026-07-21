package com.banking.platform.notificationservice.infrastructure.messaging;

import com.banking.platform.notificationservice.application.service.NotificationApplicationService;
import com.banking.platform.notificationservice.domain.model.Notification;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FraudEventConsumer {

    private final NotificationApplicationService notificationService;

    @KafkaListener(topics = "fraud-alerts", groupId = "notification-service")
    public void consumeFraudEvent(JsonNode event) {
        try {
            UUID customerId = UUID.fromString(event.get("customerId").asText());
            String riskLevel = event.has("riskLevel") ? event.get("riskLevel").asText() : "UNKNOWN";

            notificationService.sendNotification(
                    customerId,
                    Notification.NotificationType.FRAUD_ALERT,
                    "Security Alert: Suspicious Activity Detected",
                    "We detected suspicious activity on your account. Risk level: " + riskLevel +
                    ". Please contact us immediately if this was not you.",
                    customerId + "@bank.com"
            );
            log.info("Processed fraud alert for customer: {}", customerId);
        } catch (Exception e) {
            log.error("Failed to process fraud event", e);
        }
    }
}
