package com.banking.platform.customerservice.application.dto;

import com.banking.platform.customerservice.domain.model.Customer;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String taxId;
    private LocalDate dateOfBirth;
    private AddressDto address;
    private Customer.CustomerStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressDto {
        private UUID id;
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
}
