package com.banking.platform.accountservice.application.dto;

import com.banking.platform.accountservice.domain.model.Account;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private UUID id;
    private UUID customerId;
    private String accountNumber;
    private Account.AccountType type;
    private BigDecimal balance;
    private String currency;
    private Account.AccountStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
