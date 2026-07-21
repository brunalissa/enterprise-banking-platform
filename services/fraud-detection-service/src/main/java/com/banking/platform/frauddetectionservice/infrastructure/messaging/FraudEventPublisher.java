package com.banking.platform.frauddetectionservice.infrastructure.messaging;
import com.banking.platform.frauddetectionservice.infrastructure.messaging.event.FraudAlertEvent;
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
public class FraudEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topics.fraud-alerts:fraud-alerts}")
    private String topic;

    public void publishFraudAlert(FraudAlertEvent event) {
        if (event.getEventId() == null) event.setEventId(UUID.randomUUID());
        if (event.getTimestamp() == null) event.setTimestamp(Instant.now());
        log.info("Publishing FraudAlertEvent: {}", event.getAlertId());
        kafkaTemplate.send(topic, event.getCustomerId().toString(), event);
    }
}
