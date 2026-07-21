package com.banking.platform.paymentservice.domain.exception;
public class DuplicatePaymentException extends RuntimeException {
    public DuplicatePaymentException(String key) { super("Duplicate payment with idempotency key: " + key); }
}
