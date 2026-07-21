package com.banking.platform.accountservice.infrastructure.persistence.repository;

import com.banking.platform.accountservice.infrastructure.persistence.entity.AccountTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountTransactionJpaRepository extends JpaRepository<AccountTransactionEntity, UUID> {
    List<AccountTransactionEntity> findByAccountId(UUID accountId);
    Page<AccountTransactionEntity> findByAccountId(UUID accountId, Pageable pageable);
}
