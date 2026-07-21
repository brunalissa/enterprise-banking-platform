package com.banking.platform.paymentservice.infrastructure.persistence.repository;
import com.banking.platform.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByIdempotencyKey(String key);
    List<PaymentEntity> findByCustomerId(UUID customerId);
}
