package com.banking.platform.paymentservice.domain.service;

import com.banking.platform.paymentservice.domain.exception.PaymentValidationException;
import com.banking.platform.paymentservice.domain.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Slf4j
@Service
public class PaymentDomainService {
    private static final BigDecimal MAX_PAYMENT_AMOUNT = new BigDecimal("500000");

    public void validate(Payment payment) {
        if (payment.getCustomerId() == null) throw new PaymentValidationException("Customer ID is required");
        if (payment.getAccountId() == null) throw new PaymentValidationException("Account ID is required");
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new PaymentValidationException("Amount must be positive");
        if (payment.getAmount().compareTo(MAX_PAYMENT_AMOUNT) > 0)
            throw new PaymentValidationException("Amount exceeds maximum limit");
        log.debug("Payment validated: {}", payment.getId());
    }
}
