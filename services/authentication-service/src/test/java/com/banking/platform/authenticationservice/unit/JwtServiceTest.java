package com.banking.platform.authenticationservice.unit;

import com.banking.platform.authenticationservice.domain.model.User;
import com.banking.platform.authenticationservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService Unit Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField(jwtService, "secret", "banking-platform-secret-key-must-be-at-least-256-bits-long-for-hs256");
        setField(jwtService, "jwtExpiration", 86400000L);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@bank.com")
                .passwordHash("hashedPassword")
                .role(User.UserRole.CUSTOMER)
                .status(User.UserStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void shouldGenerateValidToken() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsername() {
        String token = jwtService.generateToken(testUser);
        String username = jwtService.extractUsername(token);
        assertEquals("test@bank.com", username);
    }

    @Test
    @DisplayName("Should extract role from token")
    void shouldExtractRole() {
        String token = jwtService.generateToken(testUser);
        String role = jwtService.extractRole(token);
        assertEquals("CUSTOMER", role);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateToken() {
        String token = jwtService.generateToken(testUser);
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    @DisplayName("Should return expiration in seconds")
    void shouldReturnExpirationInSeconds() {
        long seconds = jwtService.getTokenExpirationInSeconds();
        assertEquals(86400, seconds);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
