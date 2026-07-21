package com.banking.platform.customerservice.infrastructure.messaging;

import com.banking.platform.customerservice.infrastructure.messaging.event.CustomerCreatedEvent;
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
public class CustomerEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.customer-events:customer-events}")
    private String customerEventsTopic;

    public void publishCustomerCreated(CustomerCreatedEvent event) {
        if (event.getEventId() == null) event.setEventId(UUID.randomUUID());
        if (event.getTimestamp() == null) event.setTimestamp(Instant.now());

        log.info("Publishing CustomerCreatedEvent for customer: {}", event.getCustomerId());
        kafkaTemplate.send(customerEventsTopic, event.getCustomerId().toString(), event);
    }
}
