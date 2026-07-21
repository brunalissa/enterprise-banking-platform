package com.banking.platform.transactionservice.infrastructure.persistence.repository;

import com.banking.platform.transactionservice.domain.model.Transaction;
import com.banking.platform.transactionservice.domain.repository.TransactionRepository;
import com.banking.platform.transactionservice.infrastructure.persistence.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    @Override
    public Transaction save(Transaction t) {
        return toDomain(jpaRepository.save(toEntity(t)));
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Transaction> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findBySourceAccountId(UUID accountId) {
        return jpaRepository.findBySourceAccountId(accountId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private TransactionEntity toEntity(Transaction t) {
        return TransactionEntity.builder()
                .id(t.getId())
                .customerId(t.getCustomerId())
                .sourceAccountId(t.getSourceAccountId())
                .targetAccountId(t.getTargetAccountId())
                .sourceAccountNumber(t.getSourceAccountNumber())
                .targetAccountNumber(t.getTargetAccountNumber())
                .amount(t.getAmount())
                .currency(t.getCurrency())
                .type(t.getType())
                .status(t.getStatus())
                .reference(t.getReference())
                .description(t.getDescription())
                .completedAt(t.getCompletedAt())
                .build();
    }

    private Transaction toDomain(TransactionEntity e) {
        return Transaction.builder()
                .id(e.getId())
                .customerId(e.getCustomerId())
                .sourceAccountId(e.getSourceAccountId())
                .targetAccountId(e.getTargetAccountId())
                .sourceAccountNumber(e.getSourceAccountNumber())
                .targetAccountNumber(e.getTargetAccountNumber())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .type(e.getType())
                .status(e.getStatus())
                .reference(e.getReference())
                .description(e.getDescription())
                .createdAt(e.getCreatedAt())
                .completedAt(e.getCompletedAt())
                .build();
    }
}
