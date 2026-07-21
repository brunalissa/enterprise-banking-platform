package com.banking.platform.transactionservice.infrastructure.persistence.repository;

import com.banking.platform.transactionservice.infrastructure.persistence.entity.IdempotencyKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyKeyJpaRepository extends JpaRepository<IdempotencyKeyEntity, String> {
}
