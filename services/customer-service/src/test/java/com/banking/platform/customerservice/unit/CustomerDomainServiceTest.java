package com.banking.platform.customerservice.unit;

import com.banking.platform.customerservice.domain.exception.InvalidCustomerDataException;
import com.banking.platform.customerservice.domain.model.Customer;
import com.banking.platform.customerservice.domain.service.CustomerDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CustomerDomainService Unit Tests")
class CustomerDomainServiceTest {

    private CustomerDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new CustomerDomainService();
    }

    @Test
    @DisplayName("Should validate valid customer")
    void shouldValidateValidCustomer() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID()).firstName("John").lastName("Doe")
                .email("john@bank.com").taxId("123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .status(Customer.CustomerStatus.PENDING_VERIFICATION)
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();
        assertDoesNotThrow(() -> domainService.validateCustomer(customer));
    }

    @Test
    @DisplayName("Should throw when first name is blank")
    void shouldThrowWhenFirstNameBlank() {
        Customer customer = Customer.builder()
                .firstName("").lastName("Doe").email("john@bank.com").taxId("123456789").build();
        assertThrows(InvalidCustomerDataException.class, () -> domainService.validateCustomer(customer));
    }

    @Test
    @DisplayName("Should throw when email is invalid")
    void shouldThrowWhenEmailInvalid() {
        Customer customer = Customer.builder()
                .firstName("John").lastName("Doe").email("invalid-email").taxId("123456789").build();
        assertThrows(InvalidCustomerDataException.class, () -> domainService.validateCustomer(customer));
    }

    @Test
    @DisplayName("Should throw when customer is underage")
    void shouldThrowWhenUnderage() {
        Customer customer = Customer.builder()
                .firstName("John").lastName("Doe").email("john@bank.com").taxId("123456789")
                .dateOfBirth(LocalDate.now().minusYears(17)).build();
        assertThrows(InvalidCustomerDataException.class, () -> domainService.validateCustomer(customer));
    }

    @Test
    @DisplayName("Should allow suspension of active customer")
    void shouldAllowSuspensionOfActive() {
        Customer customer = Customer.builder().status(Customer.CustomerStatus.ACTIVE).build();
        assertTrue(domainService.canSuspend(customer));
    }

    @Test
    @DisplayName("Should not allow suspension of suspended customer")
    void shouldNotAllowSuspensionOfSuspended() {
        Customer customer = Customer.builder().status(Customer.CustomerStatus.SUSPENDED).build();
        assertFalse(domainService.canSuspend(customer));
    }

    @Test
    @DisplayName("Should allow activation of pending verification customer")
    void shouldAllowActivationOfPending() {
        Customer customer = Customer.builder().status(Customer.CustomerStatus.PENDING_VERIFICATION).build();
        assertTrue(domainService.canActivate(customer));
    }

    @Test
    @DisplayName("Should not allow activation of closed customer")
    void shouldNotAllowActivationOfClosed() {
        Customer customer = Customer.builder().status(Customer.CustomerStatus.CLOSED).build();
        assertFalse(domainService.canActivate(customer));
    }
}
