package com.banking.platform.transactionservice.domain.repository;

import com.banking.platform.transactionservice.domain.model.OutboxMessage;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository {
    OutboxMessage save(OutboxMessage message);
    List<OutboxMessage> findPending(int limit);
    void markProcessed(UUID id);
    void incrementRetryCount(UUID id);
}
