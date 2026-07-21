package com.banking.platform.paymentservice.application.dto;
import com.banking.platform.paymentservice.domain.model.Payment;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CreatePaymentRequest {
    @NotNull private UUID customerId;
    @NotNull private UUID accountId;
    @NotBlank @Size(max = 255) private String payee;
    @NotBlank @Size(max = 30) private String payeeAccount;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    @NotBlank @Size(max = 3) private String currency;
    @NotNull private Payment.PaymentType type;
    @Size(max = 255) private String description;
    @NotBlank private String idempotencyKey;
}
