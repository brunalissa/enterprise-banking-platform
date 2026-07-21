package com.banking.platform.accountservice.infrastructure.persistence.repository;

import com.banking.platform.accountservice.domain.model.Account;
import com.banking.platform.accountservice.domain.repository.AccountRepository;
import com.banking.platform.accountservice.infrastructure.persistence.entity.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    @Override
    public Account save(Account account) {
        return toDomain(jpaRepository.save(toEntity(account)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber).map(this::toDomain);
    }

    @Override
    public List<Account> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void updateBalance(UUID accountId, BigDecimal newBalance) {
        jpaRepository.findById(accountId).ifPresent(entity -> {
            entity.setBalance(newBalance);
            jpaRepository.save(entity);
        });
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    private AccountEntity toEntity(Account a) {
        return AccountEntity.builder()
                .id(a.getId())
                .customerId(a.getCustomerId())
                .accountNumber(a.getAccountNumber())
                .type(a.getType())
                .balance(a.getBalance())
                .currency(a.getCurrency())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    private Account toDomain(AccountEntity e) {
        return Account.builder()
                .id(e.getId())
                .customerId(e.getCustomerId())
                .accountNumber(e.getAccountNumber())
                .type(e.getType())
                .balance(e.getBalance())
                .currency(e.getCurrency())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
