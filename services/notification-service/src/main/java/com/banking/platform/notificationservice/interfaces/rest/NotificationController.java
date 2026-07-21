package com.banking.platform.notificationservice.interfaces.rest;

import com.banking.platform.notificationservice.application.service.NotificationApplicationService;
import com.banking.platform.notificationservice.domain.model.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management endpoints")
public class NotificationController {

    private final NotificationApplicationService notificationService;

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get notifications by customer")
    public ResponseEntity<List<Notification>> getByCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(notificationService.getNotificationsByCustomer(customerId));
    }

    @PostMapping
    @Operation(summary = "Send a notification manually")
    public ResponseEntity<Notification> send(
            @RequestParam UUID customerId,
            @RequestParam Notification.NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String recipient) {
        return ResponseEntity.ok(notificationService.sendNotification(customerId, type, title, message, recipient));
    }
}
