package com.banking.platform.accountservice.application.dto;

import com.banking.platform.accountservice.domain.model.Account;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Account type is required")
    private Account.AccountType type;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.00", message = "Initial balance must be non-negative")
    private BigDecimal initialBalance;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;
}
