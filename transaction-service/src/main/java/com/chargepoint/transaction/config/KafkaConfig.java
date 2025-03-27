package com.chargepoint.transaction.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.chargepont.transaction.domain.AuthorizationRequest;
import com.chargepont.transaction.domain.AuthorizationResponse;

@Configuration
@EnableKafka
public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "kafka:9092";

    
    @Bean
    public ProducerFactory<String, AuthorizationRequest> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false); // Prevent extra type info in headers
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    

    @Bean
    public KafkaTemplate<String, AuthorizationRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, AuthorizationResponse> consumerFactory() {
    	
    	Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AuthorizationResponse.class.getName());
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // Avoids type mismatch
        
        JsonDeserializer<AuthorizationResponse> jsonDeserializer = new JsonDeserializer<>(AuthorizationResponse.class);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuthorizationResponse> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuthorizationResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
