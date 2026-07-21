package com.banking.platform.authenticationservice.application.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private long expiresIn;
    private UUID userId;
    private String email;
    private String role;
}
