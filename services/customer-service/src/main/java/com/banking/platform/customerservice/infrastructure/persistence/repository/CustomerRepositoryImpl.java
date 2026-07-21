package com.banking.platform.customerservice.infrastructure.persistence.repository;

import com.banking.platform.customerservice.domain.model.Address;
import com.banking.platform.customerservice.domain.model.Customer;
import com.banking.platform.customerservice.domain.repository.CustomerRepository;
import com.banking.platform.customerservice.infrastructure.persistence.entity.AddressEmbeddable;
import com.banking.platform.customerservice.infrastructure.persistence.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    @Override
    public Customer save(Customer customer) {
        return toDomain(jpaRepository.save(toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return jpaRepository.existsByTaxId(taxId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    private CustomerEntity toEntity(Customer c) {
        return CustomerEntity.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .taxId(c.getTaxId())
                .dateOfBirth(c.getDateOfBirth())
                .address(c.getAddress() != null ? AddressEmbeddable.builder()
                        .id(c.getAddress().getId())
                        .street(c.getAddress().getStreet())
                        .city(c.getAddress().getCity())
                        .state(c.getAddress().getState())
                        .zipCode(c.getAddress().getZipCode())
                        .country(c.getAddress().getCountry())
                        .build() : null)
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

    private Customer toDomain(CustomerEntity e) {
        return Customer.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phoneNumber(e.getPhoneNumber())
                .taxId(e.getTaxId())
                .dateOfBirth(e.getDateOfBirth())
                .address(e.getAddress() != null ? Address.builder()
                        .id(e.getAddress().getId())
                        .street(e.getAddress().getStreet())
                        .city(e.getAddress().getCity())
                        .state(e.getAddress().getState())
                        .zipCode(e.getAddress().getZipCode())
                        .country(e.getAddress().getCountry())
                        .build() : null)
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
