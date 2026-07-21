package com.banking.platform.customerservice.infrastructure.persistence.entity;

import com.banking.platform.customerservice.domain.model.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "customers", schema = "customer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 20)
    private String taxId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Embedded
    private AddressEmbeddable address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Customer.CustomerStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
