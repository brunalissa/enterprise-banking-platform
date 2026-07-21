package com.banking.platform.transactionservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topics.transaction-events:transaction-events}")
    private String transactionEventsTopic;

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name(transactionEventsTopic).partitions(6).replicas(1).build();
    }
}
