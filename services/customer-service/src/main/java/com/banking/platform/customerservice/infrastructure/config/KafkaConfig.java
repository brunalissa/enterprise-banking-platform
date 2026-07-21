package com.banking.platform.customerservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.customer-events:customer-events}")
    private String customerEventsTopic;

    @Bean
    public NewTopic customerEventsTopic() {
        return TopicBuilder.name(customerEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
