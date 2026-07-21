package com.banking.platform.paymentservice.domain.repository;
import com.banking.platform.paymentservice.domain.model.Payment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    Optional<Payment> findByIdempotencyKey(String key);
    List<Payment> findByCustomerId(UUID customerId);
}
