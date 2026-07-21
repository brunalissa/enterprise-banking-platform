package com.banking.platform.transactionservice.infrastructure.messaging;

import com.banking.platform.transactionservice.domain.model.Transaction;
import com.banking.platform.transactionservice.infrastructure.messaging.event.TransactionCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.transaction-events:transaction-events}")
    private String topic;

    public void publishTransactionCompleted(Transaction tx) {
        TransactionCompletedEvent event = TransactionCompletedEvent.builder()
                .eventId(UUID.randomUUID())
                .transactionId(tx.getId())
                .customerId(tx.getCustomerId())
                .sourceAccountId(tx.getSourceAccountId())
                .targetAccountId(tx.getTargetAccountId())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .reference(tx.getReference())
                .timestamp(Instant.now())
                .build();

        log.info("Publishing TransactionCompletedEvent: {}", tx.getId());
        kafkaTemplate.send(topic, tx.getId().toString(), event);
    }
}
