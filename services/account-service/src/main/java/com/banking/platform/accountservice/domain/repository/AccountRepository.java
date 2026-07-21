package com.banking.platform.accountservice.domain.repository;

import com.banking.platform.accountservice.domain.model.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(UUID customerId);
    void updateBalance(UUID accountId, BigDecimal newBalance);
    void deleteById(UUID id);
}
