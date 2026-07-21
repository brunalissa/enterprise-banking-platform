package com.banking.platform.paymentservice.infrastructure.persistence.repository;
import com.banking.platform.paymentservice.domain.model.Payment;
import com.banking.platform.paymentservice.domain.repository.PaymentRepository;
import com.banking.platform.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;

    @Override
    public Payment save(Payment p) {
        return toDomain(jpaRepository.save(toEntity(p)));
    }
    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }
    @Override
    public Optional<Payment> findByIdempotencyKey(String key) {
        return jpaRepository.findByIdempotencyKey(key).map(this::toDomain);
    }
    @Override
    public List<Payment> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private PaymentEntity toEntity(Payment p) {
        return PaymentEntity.builder()
                .id(p.getId()).customerId(p.getCustomerId()).accountId(p.getAccountId())
                .payee(p.getPayee()).payeeAccount(p.getPayeeAccount()).amount(p.getAmount())
                .currency(p.getCurrency()).type(p.getType()).status(p.getStatus())
                .reference(p.getReference()).idempotencyKey(p.getIdempotencyKey())
                .confirmedAt(p.getConfirmedAt()).build();
    }
    private Payment toDomain(PaymentEntity e) {
        return Payment.builder()
                .id(e.getId()).customerId(e.getCustomerId()).accountId(e.getAccountId())
                .payee(e.getPayee()).payeeAccount(e.getPayeeAccount()).amount(e.getAmount())
                .currency(e.getCurrency()).type(e.getType()).status(e.getStatus())
                .reference(e.getReference()).idempotencyKey(e.getIdempotencyKey())
                .createdAt(e.getCreatedAt()).confirmedAt(e.getConfirmedAt()).build();
    }
}
