package com.banking.platform.frauddetectionservice.infrastructure.persistence.repository;
import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import com.banking.platform.frauddetectionservice.domain.repository.FraudAlertRepository;
import com.banking.platform.frauddetectionservice.infrastructure.persistence.entity.FraudAlertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FraudAlertRepositoryImpl implements FraudAlertRepository {
    private final FraudAlertJpaRepository jpaRepository;

    @Override
    public FraudAlert save(FraudAlert a) {
        return toDomain(jpaRepository.save(toEntity(a)));
    }
    @Override
    public Optional<FraudAlert> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }
    @Override
    public List<FraudAlert> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<FraudAlert> findByStatus(FraudAlert.FraudStatus status) {
        return jpaRepository.findByStatus(status).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private FraudAlertEntity toEntity(FraudAlert a) {
        return FraudAlertEntity.builder()
                .id(a.getId()).transactionId(a.getTransactionId()).customerId(a.getCustomerId())
                .amount(a.getAmount()).riskLevel(a.getRiskLevel()).reason(a.getReason())
                .status(a.getStatus()).detectedAt(a.getDetectedAt()).build();
    }
    private FraudAlert toDomain(FraudAlertEntity e) {
        return FraudAlert.builder()
                .id(e.getId()).transactionId(e.getTransactionId()).customerId(e.getCustomerId())
                .amount(e.getAmount()).riskLevel(e.getRiskLevel()).reason(e.getReason())
                .status(e.getStatus()).detectedAt(e.getDetectedAt()).build();
    }
}
