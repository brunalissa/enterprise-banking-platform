package com.banking.platform.transactionservice.interfaces.rest;

import com.banking.platform.transactionservice.application.dto.*;
import com.banking.platform.transactionservice.application.service.TransactionApplicationService;
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
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Money transfer and transaction management endpoints")
public class TransactionController {

    private final TransactionApplicationService transactionService;

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between accounts")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Transfer request from {} to {}", request.getSourceAccountId(), request.getTargetAccountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get transactions by customer")
    public ResponseEntity<List<TransactionResponse>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomer(customerId));
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get transactions by account")
    public ResponseEntity<List<TransactionResponse>> getByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(accountId));
    }
}
