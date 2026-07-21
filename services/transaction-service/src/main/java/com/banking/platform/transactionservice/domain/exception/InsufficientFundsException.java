package com.banking.platform.transactionservice.domain.exception;
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) { super(message); }
}
