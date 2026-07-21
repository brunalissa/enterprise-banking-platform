package com.banking.platform.notificationservice.domain.model;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Notification {
    private UUID id;
    private UUID customerId;
    private NotificationType type;
    private String title;
    private String message;
    private String recipient;
    private NotificationStatus status;
    private Instant createdAt;
    private Instant sentAt;

    public enum NotificationType {
        EMAIL, SMS, PUSH, ACCOUNT_ALERT, TRANSACTION_ALERT, FRAUD_ALERT
    }
    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }
}
