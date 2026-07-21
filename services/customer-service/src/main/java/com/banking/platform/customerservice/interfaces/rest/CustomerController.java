package com.banking.platform.customerservice.interfaces.rest;

import com.banking.platform.customerservice.application.dto.*;
import com.banking.platform.customerservice.application.service.CustomerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerApplicationService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("Create customer request");
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity<CustomerResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity<CustomerResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CustomerResponse> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.activateCustomer(id));
    }

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspend customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Void> suspend(@PathVariable UUID id) {
        customerService.suspendCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
