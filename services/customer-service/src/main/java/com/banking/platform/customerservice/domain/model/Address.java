package com.banking.platform.customerservice.domain.model;

import lombok.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
