package com.banking.platform.transactionservice.infrastructure.persistence.repository;

import com.banking.platform.transactionservice.infrastructure.persistence.entity.OutboxMessageEntity;
import com.banking.platform.transactionservice.domain.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxMessageEntity, UUID> {
    @Query("SELECT o FROM OutboxMessageEntity o WHERE o.status = :status ORDER BY o.createdAt ASC LIMIT :limit")
    List<OutboxMessageEntity> findPending(@Param("status") OutboxMessage.OutboxStatus status, @Param("limit") int limit);
}
