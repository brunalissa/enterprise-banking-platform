package com.banking.platform.transactionservice.domain.service;

import com.banking.platform.transactionservice.domain.model.SagaStep;
import com.banking.platform.transactionservice.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Saga Pattern Orchestrator.
 * 
 * Orchestrates a distributed transaction across multiple services:
 * 1. Reserve funds on source account
 * 2. Credit target account
 * 3. Confirm transaction
 * 
 * If any step fails, compensation is triggered in reverse order:
 * 1. Reverse credit on target account
 * 2. Release reserved funds on source account
 * 3. Mark transaction as compensated
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SagaOrchestrator {

    public SagaStep startStep(UUID sagaId, String stepName) {
        log.info("Starting saga step: {} for saga: {}", stepName, sagaId);
        return SagaStep.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .stepName(stepName)
                .status(SagaStep.SagaStepStatus.STARTED)
                .startedAt(Instant.now())
                .build();
    }

    public SagaStep completeStep(SagaStep step) {
        log.info("Completing saga step: {} for saga: {}", step.getStepName(), step.getSagaId());
        return SagaStep.toBuilder(step)
                .status(SagaStep.SagaStepStatus.COMPLETED)
                .completedAt(Instant.now())
                .build();
    }

    public SagaStep failStep(SagaStep step, String error) {
        log.error("Failing saga step: {} for saga: {} - error: {}", step.getStepName(), step.getSagaId(), error);
        return SagaStep.toBuilder(step)
                .status(SagaStep.SagaStepStatus.FAILED)
                .errorMessage(error)
                .completedAt(Instant.now())
                .build();
    }

    public SagaStep compensateStep(SagaStep step) {
        log.info("Compensating saga step: {} for saga: {}", step.getStepName(), step.getSagaId());
        return SagaStep.toBuilder(step)
                .status(SagaStep.SagaStepStatus.COMPENSATED)
                .completedAt(Instant.now())
                .build();
    }

    public Transaction markTransactionCompleted(Transaction tx) {
        log.info("Saga completed successfully for transaction: {}", tx.getId());
        return Transaction.toBuilder(tx)
                .status(Transaction.TransactionStatus.COMPLETED)
                .completedAt(Instant.now())
                .build();
    }

    public Transaction markTransactionFailed(Transaction tx, String reason) {
        log.error("Saga failed for transaction: {} - reason: {}", tx.getId(), reason);
        return Transaction.toBuilder(tx)
                .status(Transaction.TransactionStatus.FAILED)
                .completedAt(Instant.now())
                .build();
    }

    public Transaction markTransactionCompensated(Transaction tx) {
        log.info("Saga compensated for transaction: {}", tx.getId());
        return Transaction.toBuilder(tx)
                .status(Transaction.TransactionStatus.COMPENSATED)
                .completedAt(Instant.now())
                .build();
    }
}
