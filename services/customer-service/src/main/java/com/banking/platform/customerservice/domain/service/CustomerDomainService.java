package com.banking.platform.customerservice.domain.service;

import com.banking.platform.customerservice.domain.model.Customer;
import com.banking.platform.customerservice.domain.exception.InvalidCustomerDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerDomainService {

    public void validateCustomer(Customer customer) {
        if (customer.getFirstName() == null || customer.getFirstName().isBlank()) {
            throw new InvalidCustomerDataException("First name is required");
        }
        if (customer.getLastName() == null || customer.getLastName().isBlank()) {
            throw new InvalidCustomerDataException("Last name is required");
        }
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            throw new InvalidCustomerDataException("Valid email is required");
        }
        if (customer.getDateOfBirth() != null && customer.getDateOfBirth().isAfter(java.time.LocalDate.now().minusYears(18))) {
            throw new InvalidCustomerDataException("Customer must be at least 18 years old");
        }
        log.debug("Customer validation passed for: {}", customer.getEmail());
    }

    public boolean canSuspend(Customer customer) {
        return customer.getStatus() == Customer.CustomerStatus.ACTIVE;
    }

    public boolean canActivate(Customer customer) {
        return customer.getStatus() == Customer.CustomerStatus.PENDING_VERIFICATION ||
               customer.getStatus() == Customer.CustomerStatus.SUSPENDED;
    }
}
