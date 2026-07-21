package com.banking.platform.paymentservice.domain.model;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Payment {
    private UUID id;
    private UUID customerId;
    private UUID accountId;
    private String payee;
    private String payeeAccount;
    private BigDecimal amount;
    private String currency;
    private PaymentType type;
    private PaymentStatus status;
    private String reference;
    private String idempotencyKey;
    private Instant createdAt;
    private Instant confirmedAt;

    public enum PaymentType { BILL_PAYMENT, P2P_TRANSFER, MERCHANT_PAYMENT, INTERNAL_TRANSFER }
    public enum PaymentStatus { INITIATED, PROCESSING, CONFIRMED, FAILED, REFUNDED }
}
