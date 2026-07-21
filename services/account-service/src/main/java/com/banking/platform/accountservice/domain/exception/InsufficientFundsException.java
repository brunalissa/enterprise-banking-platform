package com.banking.platform.accountservice.domain.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID accountId, BigDecimal requested, BigDecimal available) {
        super("Insufficient funds for account " + accountId + ": requested " + requested + ", available " + available);
    }
}
