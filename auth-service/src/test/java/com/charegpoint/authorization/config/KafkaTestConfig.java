package com.charegpoint.authorization.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

@TestConfiguration
public class KafkaTestConfig {
    @Bean
    public KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry() {
        return Mockito.mock(KafkaListenerEndpointRegistry.class);
    }
}
