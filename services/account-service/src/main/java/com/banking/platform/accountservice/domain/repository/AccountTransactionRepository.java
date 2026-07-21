package com.banking.platform.accountservice.domain.repository;

import com.banking.platform.accountservice.domain.model.AccountTransaction;
import java.util.List;
import java.util.UUID;

public interface AccountTransactionRepository {
    AccountTransaction save(AccountTransaction transaction);
    List<AccountTransaction> findByAccountId(UUID accountId);
    List<AccountTransaction> findByAccountId(UUID accountId, int page, int size);
}
