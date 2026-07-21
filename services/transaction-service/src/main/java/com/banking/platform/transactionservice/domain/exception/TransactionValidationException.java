package com.banking.platform.transactionservice.domain.exception;
public class TransactionValidationException extends RuntimeException {
    public TransactionValidationException(String message) { super(message); }
}
