package com.banking.platform.accountservice.infrastructure.persistence.repository;

import com.banking.platform.accountservice.domain.model.AccountTransaction;
import com.banking.platform.accountservice.domain.repository.AccountTransactionRepository;
import com.banking.platform.accountservice.infrastructure.persistence.entity.AccountTransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountTransactionRepositoryImpl implements AccountTransactionRepository {

    private final AccountTransactionJpaRepository jpaRepository;

    @Override
    public AccountTransaction save(AccountTransaction tx) {
        return toDomain(jpaRepository.save(toEntity(tx)));
    }

    @Override
    public List<AccountTransaction> findByAccountId(UUID accountId) {
        return jpaRepository.findByAccountId(accountId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<AccountTransaction> findByAccountId(UUID accountId, int page, int size) {
        return jpaRepository.findByAccountId(accountId, PageRequest.of(page, size))
                .getContent().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private AccountTransactionEntity toEntity(AccountTransaction t) {
        return AccountTransactionEntity.builder()
                .id(t.getId())
                .accountId(t.getAccountId())
                .type(t.getType())
                .amount(t.getAmount())
                .currency(t.getCurrency())
                .reference(t.getReference())
                .balanceAfter(t.getBalanceAfter())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private AccountTransaction toDomain(AccountTransactionEntity e) {
        return AccountTransaction.builder()
                .id(e.getId())
                .accountId(e.getAccountId())
                .type(e.getType())
                .amount(e.getAmount())
                .currency(e.getCurrency())
                .reference(e.getReference())
                .balanceAfter(e.getBalanceAfter())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
