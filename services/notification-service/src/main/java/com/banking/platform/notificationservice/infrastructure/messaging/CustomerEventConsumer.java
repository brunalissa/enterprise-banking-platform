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
public class CustomerEventConsumer {

    private final NotificationApplicationService notificationService;

    @KafkaListener(topics = "customer-events", groupId = "notification-service")
    public void consumeCustomerEvent(JsonNode event) {
        try {
            String email = event.has("email") ? event.get("email").asText() : "unknown@bank.com";
            String fullName = event.has("fullName") ? event.get("fullName").asText() : "Customer";

            notificationService.sendNotification(
                    UUID.fromString(event.get("customerId").asText()),
                    Notification.NotificationType.EMAIL,
                    "Welcome to Enterprise Banking Platform",
                    "Dear " + fullName + ", your account has been created successfully.",
                    email
            );
            log.info("Processed customer created event for: {}", email);
        } catch (Exception e) {
            log.error("Failed to process customer event", e);
        }
    }
}
