package com.banking.platform.frauddetectionservice.infrastructure.persistence.repository;
import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import com.banking.platform.frauddetectionservice.infrastructure.persistence.entity.FraudAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface FraudAlertJpaRepository extends JpaRepository<FraudAlertEntity, UUID> {
    List<FraudAlertEntity> findByCustomerId(UUID customerId);
    List<FraudAlertEntity> findByStatus(FraudAlert.FraudStatus status);
}
