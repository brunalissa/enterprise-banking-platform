package com.banking.platform.customerservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressEmbeddable {

    @Column(name = "address_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "country", length = 100)
    private String country;
}
