package com.banking.platform.customerservice.infrastructure.persistence.repository;

import com.banking.platform.customerservice.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTaxId(String taxId);
}
