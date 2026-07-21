package com.banking.platform.notificationservice.domain.repository;
import com.banking.platform.notificationservice.domain.model.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
    List<Notification> findByCustomerId(UUID customerId);
    List<Notification> findPending();
}
