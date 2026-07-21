package com.banking.platform.paymentservice.infrastructure.messaging;
import com.banking.platform.paymentservice.infrastructure.messaging.event.PaymentConfirmedEvent;
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
public class PaymentEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topics.payment-events:payment-events}")
    private String topic;

    public void publishPaymentConfirmed(PaymentConfirmedEvent event) {
        if (event.getEventId() == null) event.setEventId(UUID.randomUUID());
        if (event.getTimestamp() == null) event.setTimestamp(Instant.now());
        log.info("Publishing PaymentConfirmedEvent: {}", event.getPaymentId());
        kafkaTemplate.send(topic, event.getPaymentId().toString(), event);
    }
}
