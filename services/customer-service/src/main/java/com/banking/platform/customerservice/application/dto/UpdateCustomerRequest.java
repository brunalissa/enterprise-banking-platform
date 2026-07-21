package com.banking.platform.customerservice.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerRequest {
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String lastName;
    @Email
    private String email;
    @Size(max = 20)
    private String phoneNumber;
    private CreateCustomerRequest.AddressDto address;
}
