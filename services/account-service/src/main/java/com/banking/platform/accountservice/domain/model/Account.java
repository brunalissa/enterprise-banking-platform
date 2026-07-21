package com.banking.platform.accountservice.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private UUID customerId;
    private String accountNumber;
    private AccountType type;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public enum AccountType {
        CHECKING, SAVINGS, BUSINESS, JOINT
    }

    public enum AccountStatus {
        ACTIVE, FROZEN, CLOSED, PENDING_ACTIVATION
    }

    public boolean canWithdraw(BigDecimal amount) {
        return this.status == AccountStatus.ACTIVE && this.balance.compareTo(amount) >= 0;
    }

    public boolean isFrozen() {
        return this.status == AccountStatus.FROZEN;
    }
}
