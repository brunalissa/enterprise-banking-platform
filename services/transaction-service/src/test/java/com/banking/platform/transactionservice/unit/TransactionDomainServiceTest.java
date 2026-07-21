package com.banking.platform.transactionservice.unit;

import com.banking.platform.transactionservice.domain.exception.TransactionValidationException;
import com.banking.platform.transactionservice.domain.model.Transaction;
import com.banking.platform.transactionservice.domain.service.TransactionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TransactionDomainService Unit Tests")
class TransactionDomainServiceTest {

    private TransactionDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new TransactionDomainService();
    }

    @Test
    @DisplayName("Should validate valid transaction")
    void shouldValidateValidTransaction() {
        Transaction tx = createValidTransaction();
        assertDoesNotThrow(() -> domainService.validate(tx));
    }

    @Test
    @DisplayName("Should throw when customer id is null")
    void shouldThrowWhenCustomerIdNull() {
        Transaction tx = createValidTransaction();
        Transaction modified = Transaction.toBuilder(tx).customerId(null).build();
        assertThrows(TransactionValidationException.class, () -> domainService.validate(modified));
    }

    @Test
    @DisplayName("Should throw when same source and target accounts")
    void shouldThrowWhenSameAccounts() {
        UUID same = UUID.randomUUID();
        Transaction tx = createValidTransaction();
        Transaction modified = Transaction.toBuilder(tx).sourceAccountId(same).targetAccountId(same).build();
        assertThrows(TransactionValidationException.class, () -> domainService.validate(modified));
    }

    @Test
    @DisplayName("Should throw when amount is zero or negative")
    void shouldThrowWhenAmountZeroOrNegative() {
        Transaction tx = createValidTransaction();
        Transaction modified = Transaction.toBuilder(tx).amount(BigDecimal.ZERO).build();
        assertThrows(TransactionValidationException.class, () -> domainService.validate(modified));
    }

    @Test
    @DisplayName("Should throw when amount exceeds maximum")
    void shouldThrowWhenAmountExceedsMax() {
        Transaction tx = createValidTransaction();
        Transaction modified = Transaction.toBuilder(tx).amount(new BigDecimal("2000000")).build();
        assertThrows(TransactionValidationException.class, () -> domainService.validate(modified));
    }

    private Transaction createValidTransaction() {
        return Transaction.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .sourceAccountId(UUID.randomUUID()).targetAccountId(UUID.randomUUID())
                .amount(new BigDecimal("1000.00")).currency("USD")
                .type(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.PENDING)
                .createdAt(Instant.now()).build();
    }
}