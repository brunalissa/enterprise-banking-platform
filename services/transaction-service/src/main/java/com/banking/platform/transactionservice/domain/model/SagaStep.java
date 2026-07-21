package com.banking.platform.transactionservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SagaStep {
    private UUID id;
    private UUID sagaId;
    private String stepName;
    private SagaStepStatus status;
    private String errorMessage;
    private Instant startedAt;
    private Instant completedAt;

    public enum SagaStepStatus {
        STARTED, COMPLETED, FAILED, COMPENSATED, COMPENSATION_FAILED
    }

    public static SagaStepBuilder toBuilder(SagaStep step) {
        return SagaStep.builder()
                .id(step.getId())
                .sagaId(step.getSagaId())
                .stepName(step.getStepName())
                .status(step.getStatus())
                .errorMessage(step.getErrorMessage())
                .startedAt(step.getStartedAt())
                .completedAt(step.getCompletedAt());
    }
}
