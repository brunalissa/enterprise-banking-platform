package com.banking.platform.transactionservice.infrastructure.persistence.repository;

import com.banking.platform.transactionservice.domain.model.OutboxMessage;
import com.banking.platform.transactionservice.domain.repository.OutboxRepository;
import com.banking.platform.transactionservice.infrastructure.persistence.entity.OutboxMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository jpaRepository;

    @Override
    public OutboxMessage save(OutboxMessage m) {
        return toDomain(jpaRepository.save(toEntity(m)));
    }

    @Override
    public List<OutboxMessage> findPending(int limit) {
        return jpaRepository.findPending(OutboxMessage.OutboxStatus.PENDING, limit).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void markProcessed(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setStatus(OutboxMessage.OutboxStatus.PROCESSED);
            e.setProcessedAt(Instant.now());
            jpaRepository.save(e);
        });
    }

    @Override
    public void incrementRetryCount(UUID id) {
        jpaRepository.findById(id).ifPresent(e -> {
            e.setRetryCount(e.getRetryCount() + 1);
            jpaRepository.save(e);
        });
    }

    private OutboxMessageEntity toEntity(OutboxMessage m) {
        return OutboxMessageEntity.builder()
                .id(m.getId())
                .aggregateType(m.getAggregateType())
                .aggregateId(m.getAggregateId())
                .eventType(m.getEventType())
                .payload(m.getPayload())
                .status(m.getStatus())
                .createdAt(m.getCreatedAt())
                .processedAt(m.getProcessedAt())
                .retryCount(m.getRetryCount())
                .build();
    }

    private OutboxMessage toDomain(OutboxMessageEntity e) {
        return OutboxMessage.builder()
                .id(e.getId())
                .aggregateType(e.getAggregateType())
                .aggregateId(e.getAggregateId())
                .eventType(e.getEventType())
                .payload(e.getPayload())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .processedAt(e.getProcessedAt())
                .retryCount(e.getRetryCount())
                .build();
    }
}
