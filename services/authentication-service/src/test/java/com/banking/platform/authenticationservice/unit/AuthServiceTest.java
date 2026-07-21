package com.banking.platform.authenticationservice.unit;

import com.banking.platform.authenticationservice.application.dto.AuthRequest;
import com.banking.platform.authenticationservice.application.dto.AuthResponse;
import com.banking.platform.authenticationservice.application.dto.RegisterRequest;
import com.banking.platform.authenticationservice.application.service.AuthService;
import com.banking.platform.authenticationservice.domain.exception.InvalidCredentialsException;
import com.banking.platform.authenticationservice.domain.exception.UserAlreadyExistsException;
import com.banking.platform.authenticationservice.domain.model.User;
import com.banking.platform.authenticationservice.domain.repository.UserRepository;
import com.banking.platform.authenticationservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthService authService;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("test@bank.com")
                .password("password123")
                .role(User.UserRole.CUSTOMER)
                .build();

        authRequest = AuthRequest.builder()
                .email("test@bank.com")
                .password("password123")
                .build();

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
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUser() {
        when(userRepository.existsByEmail("test@bank.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
        when(jwtService.getTokenExpirationInSeconds()).thenReturn(86400L);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("test@bank.com", response.getEmail());
        assertEquals("CUSTOMER", response.getRole());
        assertEquals("jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email exists")
    void shouldThrowWhenEmailExists() {
        when(userRepository.existsByEmail("test@bank.com")).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registerRequest));
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldAuthenticateUser() {
        when(userRepository.findByEmail("test@bank.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn("jwt-token");
        when(jwtService.getTokenExpirationInSeconds()).thenReturn(86400L);

        AuthResponse response = authService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("test@bank.com", response.getEmail());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException for wrong password")
    void shouldThrowForWrongPassword() {
        when(userRepository.findByEmail("test@bank.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpass", "hashedPassword")).thenReturn(false);

        authRequest = AuthRequest.builder().email("test@bank.com").password("wrongpass").build();
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(authRequest));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException for non-existent user")
    void shouldThrowForNonExistentUser() {
        when(userRepository.findByEmail("test@bank.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(authRequest));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when account locked")
    void shouldThrowWhenAccountLocked() {
        User lockedUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@bank.com")
                .passwordHash("hashedPassword")
                .role(User.UserRole.CUSTOMER)
                .status(User.UserStatus.LOCKED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(userRepository.findByEmail("test@bank.com")).thenReturn(Optional.of(lockedUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(authRequest));
    }
}
