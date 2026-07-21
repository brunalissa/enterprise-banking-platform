package com.banking.platform.accountservice.interfaces.rest;

import com.banking.platform.accountservice.application.dto.*;
import com.banking.platform.accountservice.application.service.AccountApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Bank account management endpoints")
public class AccountController {

    private final AccountApplicationService accountService;

    @PostMapping
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        log.info("Create account for customer: {}", request.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<AccountResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by account number")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all accounts for a customer")
    public ResponseEntity<List<AccountResponse>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomer(customerId));
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get account transaction history")
    public ResponseEntity<List<TransactionResponse>> history(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(accountService.getAccountHistory(id, page, size));
    }

    @PostMapping("/{id}/freeze")
    @Operation(summary = "Freeze an account")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AccountResponse> freeze(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.freezeAccount(id));
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "Close an account")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AccountResponse> close(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.closeAccount(id));
    }
}
