package com.banking.platform.customerservice.domain.repository;

import com.banking.platform.customerservice.domain.model.Customer;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTaxId(String taxId);
    void deleteById(UUID id);
}
