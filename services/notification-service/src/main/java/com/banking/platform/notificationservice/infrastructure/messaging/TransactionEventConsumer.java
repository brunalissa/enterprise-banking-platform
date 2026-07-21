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
public class TransactionEventConsumer {

    private final NotificationApplicationService notificationService;

    @KafkaListener(topics = "transaction-events", groupId = "notification-service")
    public void consumeTransactionEvent(JsonNode event) {
        try {
            UUID customerId = UUID.fromString(event.get("customerId").asText());
            String reference = event.has("reference") ? event.get("reference").asText() : "Unknown";
            String amount = event.has("amount") ? event.get("amount").asText() : "0";

            notificationService.sendNotification(
                    customerId,
                    Notification.NotificationType.TRANSACTION_ALERT,
                    "Transaction Completed",
                    "Your transaction " + reference + " for " + amount + " has been completed.",
                    customerId + "@bank.com"
            );
            log.info("Processed transaction event for customer: {}", customerId);
        } catch (Exception e) {
            log.error("Failed to process transaction event", e);
        }
    }
}
