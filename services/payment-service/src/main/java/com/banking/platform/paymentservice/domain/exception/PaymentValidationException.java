package com.banking.platform.paymentservice.domain.exception;
public class PaymentValidationException extends RuntimeException {
    public PaymentValidationException(String msg) { super(msg); }
}
