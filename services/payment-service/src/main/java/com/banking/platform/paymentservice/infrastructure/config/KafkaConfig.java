package com.banking.platform.paymentservice.infrastructure.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${app.kafka.topics.payment-events:payment-events}")
    private String topic;
    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }
}
