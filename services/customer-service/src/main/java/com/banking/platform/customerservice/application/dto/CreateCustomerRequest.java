package com.banking.platform.customerservice.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    @NotBlank(message = "Tax ID is required")
    @Size(max = 20)
    private String taxId;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private AddressDto address;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressDto {
        @NotBlank private String street;
        @NotBlank private String city;
        @NotBlank private String state;
        @NotBlank private String zipCode;
        @NotBlank private String country;
    }
}
