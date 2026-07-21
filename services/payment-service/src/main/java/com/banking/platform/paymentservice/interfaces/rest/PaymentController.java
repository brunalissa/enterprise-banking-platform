package com.banking.platform.paymentservice.interfaces.rest;

import com.banking.platform.paymentservice.application.dto.*;
import com.banking.platform.paymentservice.application.service.PaymentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing endpoints")
public class PaymentController {

    private final PaymentApplicationService paymentService;

    @PostMapping
    @Operation(summary = "Process a new payment")
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody CreatePaymentRequest request) {
        log.info("Payment request from customer: {}", request.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get payments by customer")
    public ResponseEntity<List<PaymentResponse>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }
}
