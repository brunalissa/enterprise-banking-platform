package com.banking.platform.notificationservice.infrastructure.persistence.entity;
import com.banking.platform.notificationservice.domain.model.Notification;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "notification")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class NotificationEntity {
    @Id @Column(columnDefinition = "uuid") private UUID id;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private Notification.NotificationType type;
    @Column(nullable = false, length = 255) private String title;
    @Column(nullable = false, length = 1000) private String message;
    @Column(length = 255) private String recipient;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Notification.NotificationStatus status;
    @CreationTimestamp @Column(name = "created_at", updatable = false) private Instant createdAt;
    @Column(name = "sent_at") private Instant sentAt;
}
