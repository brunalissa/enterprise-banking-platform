package com.banking.platform.frauddetectionservice.domain.repository;
import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FraudAlertRepository {
    FraudAlert save(FraudAlert alert);
    Optional<FraudAlert> findById(UUID id);
    List<FraudAlert> findByCustomerId(UUID customerId);
    List<FraudAlert> findByStatus(FraudAlert.FraudStatus status);
}
