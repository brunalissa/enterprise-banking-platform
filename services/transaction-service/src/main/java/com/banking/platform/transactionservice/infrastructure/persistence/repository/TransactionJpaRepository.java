package com.banking.platform.transactionservice.infrastructure.persistence.repository;

import com.banking.platform.transactionservice.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByCustomerId(UUID customerId);
    List<TransactionEntity> findBySourceAccountId(UUID accountId);
}
