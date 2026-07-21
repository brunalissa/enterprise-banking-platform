package com.banking.platform.notificationservice.infrastructure.persistence.repository;
import com.banking.platform.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import com.banking.platform.notificationservice.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, UUID> {
    List<NotificationEntity> findByCustomerId(UUID customerId);
    List<NotificationEntity> findByStatus(Notification.NotificationStatus status);
}
