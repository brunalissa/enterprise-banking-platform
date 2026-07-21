package com.banking.platform.transactionservice.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Source account ID is required")
    private UUID sourceAccountId;

    @NotNull(message = "Target account ID is required")
    private UUID targetAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3)
    private String currency;

    @Size(max = 255)
    private String description;

    private String idempotencyKey;
}
