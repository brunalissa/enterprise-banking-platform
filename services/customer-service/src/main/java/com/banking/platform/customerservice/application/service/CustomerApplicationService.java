package com.banking.platform.customerservice.application.service;

import com.banking.platform.customerservice.application.dto.*;
import com.banking.platform.customerservice.domain.exception.*;
import com.banking.platform.customerservice.domain.model.*;
import com.banking.platform.customerservice.domain.repository.CustomerRepository;
import com.banking.platform.customerservice.domain.service.CustomerDomainService;
import com.banking.platform.customerservice.infrastructure.messaging.event.CustomerCreatedEvent;
import com.banking.platform.customerservice.infrastructure.messaging.CustomerEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerApplicationService {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService domainService;
    private final CustomerEventPublisher eventPublisher;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer: {}", request.getEmail());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Email already registered: " + request.getEmail());
        }
        if (customerRepository.existsByTaxId(request.getTaxId())) {
            throw new CustomerAlreadyExistsException("Tax ID already registered");
        }

        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .taxId(request.getTaxId())
                .dateOfBirth(request.getDateOfBirth())
                .address(mapAddress(request.getAddress()))
                .status(Customer.CustomerStatus.PENDING_VERIFICATION)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        domainService.validateCustomer(customer);
        Customer saved = customerRepository.save(customer);

        eventPublisher.publishCustomerCreated(CustomerCreatedEvent.builder()
                .customerId(saved.getId())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .build());

        log.info("Customer created: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {
        log.info("Updating customer: {}", id);

        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        Customer updated = Customer.builder()
                .id(existing.getId())
                .firstName(request.getFirstName() != null ? request.getFirstName() : existing.getFirstName())
                .lastName(request.getLastName() != null ? request.getLastName() : existing.getLastName())
                .email(request.getEmail() != null ? request.getEmail() : existing.getEmail())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : existing.getPhoneNumber())
                .taxId(existing.getTaxId())
                .dateOfBirth(existing.getDateOfBirth())
                .address(request.getAddress() != null ? mapAddress(request.getAddress()) : existing.getAddress())
                .status(existing.getStatus())
                .createdAt(existing.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        domainService.validateCustomer(updated);
        Customer saved = customerRepository.save(updated);

        log.info("Customer updated: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(UUID id) {
        return customerRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    public CustomerResponse activateCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (!domainService.canActivate(customer)) {
            throw new InvalidCustomerDataException("Customer cannot be activated from status: " + customer.getStatus());
        }

        Customer activated = Customer.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .taxId(customer.getTaxId())
                .dateOfBirth(customer.getDateOfBirth())
                .address(customer.getAddress())
                .status(Customer.CustomerStatus.ACTIVE)
                .createdAt(customer.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        Customer saved = customerRepository.save(activated);
        log.info("Customer activated: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional
    public void suspendCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (!domainService.canSuspend(customer)) {
            throw new InvalidCustomerDataException("Customer cannot be suspended from status: " + customer.getStatus());
        }

        Customer suspended = Customer.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .taxId(customer.getTaxId())
                .dateOfBirth(customer.getDateOfBirth())
                .address(customer.getAddress())
                .status(Customer.CustomerStatus.SUSPENDED)
                .createdAt(customer.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        customerRepository.save(suspended);
        log.info("Customer suspended: {}", id);
    }

    private Address mapAddress(CreateCustomerRequest.AddressDto dto) {
        if (dto == null) return null;
        return Address.builder()
                .id(UUID.randomUUID())
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .country(dto.getCountry())
                .build();
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .fullName(c.getFullName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .taxId(c.getTaxId())
                .dateOfBirth(c.getDateOfBirth())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .address(c.getAddress() != null ? CustomerResponse.AddressDto.builder()
                        .id(c.getAddress().getId())
                        .street(c.getAddress().getStreet())
                        .city(c.getAddress().getCity())
                        .state(c.getAddress().getState())
                        .zipCode(c.getAddress().getZipCode())
                        .country(c.getAddress().getCountry())
                        .build() : null)
                .build();
    }
}
