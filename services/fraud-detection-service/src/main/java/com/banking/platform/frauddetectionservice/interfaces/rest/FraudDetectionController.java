package com.banking.platform.frauddetectionservice.interfaces.rest;

import com.banking.platform.frauddetectionservice.application.service.FraudDetectionApplicationService;
import com.banking.platform.frauddetectionservice.domain.model.FraudAlert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
@Tag(name = "Fraud Detection", description = "Fraud detection and alert management endpoints")
public class FraudDetectionController {

    private final FraudDetectionApplicationService fraudService;

    @GetMapping("/alerts/customer/{customerId}")
    @Operation(summary = "Get fraud alerts by customer")
    public ResponseEntity<List<FraudAlert>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(fraudService.getAlertsByCustomer(customerId));
    }

    @GetMapping("/alerts/open")
    @Operation(summary = "Get all open fraud alerts")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<FraudAlert>> getOpenAlerts() {
        return ResponseEntity.ok(fraudService.getOpenAlerts());
    }

    @PutMapping("/alerts/{alertId}/status")
    @Operation(summary = "Update fraud alert status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<FraudAlert> updateStatus(@PathVariable UUID alertId, @RequestParam FraudAlert.FraudStatus status) {
        return ResponseEntity.ok(fraudService.updateAlertStatus(alertId, status));
    }
}
