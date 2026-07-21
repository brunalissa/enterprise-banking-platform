package com.banking.platform.customerservice.domain.exception;

public class InvalidCustomerDataException extends RuntimeException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
