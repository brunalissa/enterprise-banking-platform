package com.banking.platform.accountservice.infrastructure.messaging;

import com.banking.platform.accountservice.infrastructure.messaging.event.AccountCreatedEvent;
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
public class AccountEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.account-events:account-events}")
    private String accountEventsTopic;

    public void publishAccountCreated(AccountCreatedEvent event) {
        if (event.getEventId() == null) event.setEventId(UUID.randomUUID());
        if (event.getTimestamp() == null) event.setTimestamp(Instant.now());

        log.info("Publishing AccountCreatedEvent for account: {}", event.getAccountId());
        kafkaTemplate.send(accountEventsTopic, event.getAccountId().toString(), event);
    }
}
