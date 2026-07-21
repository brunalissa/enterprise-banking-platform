package com.banking.platform.customerservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String taxId;
    private LocalDate dateOfBirth;
    private Address address;
    private CustomerStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public enum CustomerStatus {
        PENDING_VERIFICATION, ACTIVE, SUSPENDED, BLACKLISTED, CLOSED
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean canTransact() {
        return this.status == CustomerStatus.ACTIVE;
    }
}
