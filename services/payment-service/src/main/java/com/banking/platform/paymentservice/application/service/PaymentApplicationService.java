package com.banking.platform.paymentservice.application.service;

import com.banking.platform.paymentservice.application.dto.*;
import com.banking.platform.paymentservice.domain.exception.*;
import com.banking.platform.paymentservice.domain.model.Payment;
import com.banking.platform.paymentservice.domain.repository.PaymentRepository;
import com.banking.platform.paymentservice.domain.service.PaymentDomainService;
import com.banking.platform.paymentservice.infrastructure.messaging.PaymentEventPublisher;
import com.banking.platform.paymentservice.infrastructure.messaging.event.PaymentConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;
    private final PaymentDomainService domainService;
    private final PaymentEventPublisher eventPublisher;

    @Transactional
    public PaymentResponse processPayment(CreatePaymentRequest request) {
        // Idempotency check
        var existing = paymentRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            log.info("Idempotent payment detected, returning existing: {}", existing.get().getId());
            return toResponse(existing.get());
        }

        log.info("Processing payment of {} {} for customer: {}", request.getAmount(), request.getCurrency(), request.getCustomerId());

        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .customerId(request.getCustomerId())
                .accountId(request.getAccountId())
                .payee(request.getPayee())
                .payeeAccount(request.getPayeeAccount())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(request.getType())
                .status(Payment.PaymentStatus.INITIATED)
                .reference("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .idempotencyKey(request.getIdempotencyKey())
                .createdAt(Instant.now())
                .build();

        domainService.validate(payment);

        // Transition to PROCESSING
        payment = Payment.builder()
                .id(payment.getId())
                .customerId(payment.getCustomerId())
                .accountId(payment.getAccountId())
                .payee(payment.getPayee())
                .payeeAccount(payment.getPayeeAccount())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .type(payment.getType())
                .status(Payment.PaymentStatus.PROCESSING)
                .reference(payment.getReference())
                .idempotencyKey(payment.getIdempotencyKey())
                .createdAt(payment.getCreatedAt())
                .build();

        Payment saved = paymentRepository.save(payment);

        // Transition to CONFIRMED (in production this would be async with retries)
        saved = Payment.builder()
                .id(saved.getId())
                .customerId(saved.getCustomerId())
                .accountId(saved.getAccountId())
                .payee(saved.getPayee())
                .payeeAccount(saved.getPayeeAccount())
                .amount(saved.getAmount())
                .currency(saved.getCurrency())
                .type(saved.getType())
                .status(Payment.PaymentStatus.CONFIRMED)
                .reference(saved.getReference())
                .idempotencyKey(saved.getIdempotencyKey())
                .createdAt(saved.getCreatedAt())
                .confirmedAt(Instant.now())
                .build();

        saved = paymentRepository.save(saved);

        eventPublisher.publishPaymentConfirmed(PaymentConfirmedEvent.builder()
                .paymentId(saved.getId())
                .customerId(saved.getCustomerId())
                .amount(saved.getAmount())
                .currency(saved.getCurrency())
                .reference(saved.getReference())
                .build());

        log.info("Payment confirmed: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID id) {
        return paymentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByCustomer(UUID customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId()).customerId(p.getCustomerId()).accountId(p.getAccountId())
                .payee(p.getPayee()).payeeAccount(p.getPayeeAccount())
                .amount(p.getAmount()).currency(p.getCurrency())
                .type(p.getType()).status(p.getStatus())
                .reference(p.getReference()).createdAt(p.getCreatedAt())
                .confirmedAt(p.getConfirmedAt()).build();
    }
}
