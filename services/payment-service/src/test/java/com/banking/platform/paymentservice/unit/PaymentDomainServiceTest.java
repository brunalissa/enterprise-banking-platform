package com.banking.platform.paymentservice.unit;

import com.banking.platform.paymentservice.domain.exception.PaymentValidationException;
import com.banking.platform.paymentservice.domain.model.Payment;
import com.banking.platform.paymentservice.domain.service.PaymentDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentDomainService Unit Tests")
class PaymentDomainServiceTest {

    private PaymentDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new PaymentDomainService();
    }

    @Test
    @DisplayName("Should validate valid payment")
    void shouldValidateValidPayment() {
        assertDoesNotThrow(() -> domainService.validate(createValidPayment()));
    }

    @Test
    @DisplayName("Should throw when customer ID is null")
    void shouldThrowWhenCustomerIdNull() {
        Payment valid = createValidPayment();
        Payment modified = Payment.builder()
                .id(valid.getId()).customerId(null).accountId(valid.getAccountId())
                .payee(valid.getPayee()).payeeAccount(valid.getPayeeAccount())
                .amount(valid.getAmount()).currency(valid.getCurrency())
                .type(valid.getType()).status(valid.getStatus())
                .idempotencyKey(valid.getIdempotencyKey()).createdAt(valid.getCreatedAt()).build();
        assertThrows(PaymentValidationException.class, () -> domainService.validate(modified));
    }

    @Test
    @DisplayName("Should throw when amount is zero or negative")
    void shouldThrowWhenAmountZeroOrNegative() {
        Payment valid = createValidPayment();
        Payment modified = Payment.builder()
                .id(valid.getId()).customerId(valid.getCustomerId()).accountId(valid.getAccountId())
                .payee(valid.getPayee()).payeeAccount(valid.getPayeeAccount())
                .amount(BigDecimal.ZERO).currency(valid.getCurrency())
                .type(valid.getType()).status(valid.getStatus())
                .idempotencyKey(valid.getIdempotencyKey()).createdAt(valid.getCreatedAt()).build();
        assertThrows(PaymentValidationException.class, () -> domainService.validate(modified));
    }

    @Test
    @DisplayName("Should throw when amount exceeds maximum")
    void shouldThrowWhenAmountExceedsMax() {
        Payment valid = createValidPayment();
        Payment modified = Payment.builder()
                .id(valid.getId()).customerId(valid.getCustomerId()).accountId(valid.getAccountId())
                .payee(valid.getPayee()).payeeAccount(valid.getPayeeAccount())
                .amount(new BigDecimal("600000")).currency(valid.getCurrency())
                .type(valid.getType()).status(valid.getStatus())
                .idempotencyKey(valid.getIdempotencyKey()).createdAt(valid.getCreatedAt()).build();
        assertThrows(PaymentValidationException.class, () -> domainService.validate(modified));
    }

    private Payment createValidPayment() {
        return Payment.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID()).accountId(UUID.randomUUID())
                .payee("Merchant").payeeAccount("ACC123")
                .amount(new BigDecimal("100.00")).currency("USD")
                .type(Payment.PaymentType.MERCHANT_PAYMENT)
                .status(Payment.PaymentStatus.INITIATED)
                .idempotencyKey("key-123").createdAt(Instant.now()).build();
    }
}