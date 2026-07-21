package com.banking.platform.notificationservice.infrastructure.persistence.repository;
import com.banking.platform.notificationservice.domain.model.Notification;
import com.banking.platform.notificationservice.domain.repository.NotificationRepository;
import com.banking.platform.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository jpaRepository;

    @Override
    public Notification save(Notification n) {
        return toDomain(jpaRepository.save(toEntity(n)));
    }
    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }
    @Override
    public List<Notification> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<Notification> findPending() {
        return jpaRepository.findByStatus(Notification.NotificationStatus.PENDING).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private NotificationEntity toEntity(Notification n) {
        return NotificationEntity.builder()
                .id(n.getId()).customerId(n.getCustomerId()).type(n.getType())
                .title(n.getTitle()).message(n.getMessage()).recipient(n.getRecipient())
                .status(n.getStatus()).sentAt(n.getSentAt()).build();
    }
    private Notification toDomain(NotificationEntity e) {
        return Notification.builder()
                .id(e.getId()).customerId(e.getCustomerId()).type(e.getType())
                .title(e.getTitle()).message(e.getMessage()).recipient(e.getRecipient())
                .status(e.getStatus()).createdAt(e.getCreatedAt()).sentAt(e.getSentAt()).build();
    }
}
