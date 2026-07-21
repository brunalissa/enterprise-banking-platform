package com.banking.platform.accountservice.unit;

import com.banking.platform.accountservice.domain.exception.InsufficientFundsException;
import com.banking.platform.accountservice.domain.model.Account;
import com.banking.platform.accountservice.domain.model.AccountTransaction;
import com.banking.platform.accountservice.domain.service.AccountDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccountDomainService Unit Tests")
class AccountDomainServiceTest {

    private AccountDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new AccountDomainService();
    }

    @Test
    @DisplayName("Should deposit to active account")
    void shouldDepositToActiveAccount() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .balance(new BigDecimal("1000.00")).currency("USD")
                .status(Account.AccountStatus.ACTIVE).build();

        AccountTransaction tx = domainService.deposit(account, new BigDecimal("500.00"), "REF001");

        assertNotNull(tx);
        assertEquals(AccountTransaction.TransactionType.CREDIT, tx.getType());
        assertEquals(new BigDecimal("1500.00"), tx.getBalanceAfter());
    }

    @Test
    @DisplayName("Should withdraw with sufficient funds")
    void shouldWithdrawWithSufficientFunds() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .balance(new BigDecimal("1000.00")).currency("USD")
                .status(Account.AccountStatus.ACTIVE).build();

        AccountTransaction tx = domainService.withdraw(account, new BigDecimal("500.00"), "REF002");

        assertNotNull(tx);
        assertEquals(AccountTransaction.TransactionType.DEBIT, tx.getType());
        assertEquals(new BigDecimal("500.00"), tx.getBalanceAfter());
    }

    @Test
    @DisplayName("Should throw InsufficientFundsException when balance too low")
    void shouldThrowWhenInsufficientFunds() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .balance(new BigDecimal("100.00")).currency("USD")
                .status(Account.AccountStatus.ACTIVE).build();

        assertThrows(InsufficientFundsException.class,
                () -> domainService.withdraw(account, new BigDecimal("500.00"), "REF003"));
    }

    @Test
    @DisplayName("Should throw when account is frozen")
    void shouldThrowWhenFrozen() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .balance(new BigDecimal("1000.00")).currency("USD")
                .status(Account.AccountStatus.FROZEN).build();

        assertThrows(IllegalStateException.class,
                () -> domainService.deposit(account, new BigDecimal("100.00"), "REF004"));
        assertThrows(IllegalStateException.class,
                () -> domainService.withdraw(account, new BigDecimal("100.00"), "REF005"));
    }

    @Test
    @DisplayName("Should validate valid new account")
    void shouldValidateValidNewAccount() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(UUID.randomUUID())
                .balance(new BigDecimal("1000.00")).currency("USD").build();
        assertDoesNotThrow(() -> domainService.validateNewAccount(account));
    }

    @Test
    @DisplayName("Should throw when customer id is null")
    void shouldThrowWhenCustomerIdNull() {
        Account account = Account.builder()
                .id(UUID.randomUUID()).customerId(null)
                .balance(new BigDecimal("1000.00")).currency("USD").build();
        assertThrows(IllegalArgumentException.class, () -> domainService.validateNewAccount(account));
    }
}
