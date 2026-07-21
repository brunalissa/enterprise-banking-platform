package com.banking.platform.notificationservice.application.service;

import com.banking.platform.notificationservice.domain.model.Notification;
import com.banking.platform.notificationservice.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationApplicationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification sendNotification(UUID customerId, Notification.NotificationType type,
                                          String title, String message, String recipient) {
        log.info("Sending {} notification to customer: {}", type, customerId);

        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .type(type)
                .title(title)
                .message(message)
                .recipient(recipient)
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        // Simulate sending (in production, integrate with email/SMS provider)
        notification = Notification.builder()
                .id(notification.getId())
                .customerId(notification.getCustomerId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .recipient(notification.getRecipient())
                .status(Notification.NotificationStatus.SENT)
                .createdAt(notification.getCreatedAt())
                .sentAt(Instant.now())
                .build();

        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByCustomer(UUID customerId) {
        return notificationRepository.findByCustomerId(customerId);
    }
}
