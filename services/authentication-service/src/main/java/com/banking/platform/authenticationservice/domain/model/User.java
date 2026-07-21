package com.banking.platform.authenticationservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String passwordHash;
    private UserRole role;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public enum UserRole {
        ADMIN, CUSTOMER, EMPLOYEE
    }

    public enum UserStatus {
        ACTIVE, LOCKED, SUSPENDED, PENDING_VERIFICATION
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
}
