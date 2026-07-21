package com.banking.platform.paymentservice.application.dto;
import com.banking.platform.paymentservice.domain.model.Payment;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private UUID id;
    private UUID customerId;
    private UUID accountId;
    private String payee;
    private String payeeAccount;
    private BigDecimal amount;
    private String currency;
    private Payment.PaymentType type;
    private Payment.PaymentStatus status;
    private String reference;
    private Instant createdAt;
    private Instant confirmedAt;
}
