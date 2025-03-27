package com.chargepoint.authorization.config;

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

import com.chargepoint.authorization.domain.AuthorizationRequest;
import com.chargepoint.authorization.domain.AuthorizationResponse;

@Configuration
@EnableKafka
public class KafkaConfig {

	//@Value("${spring.kafka.bootstrap-servers}")
    //private String bootstrapServers;

	private static final String BOOTSTRAP_SERVERS = "kafka:9092";


	@Bean
	public ProducerFactory<String, AuthorizationResponse> producerFactory() {
	    Map<String, Object> configProps = new HashMap<>();
	    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
	    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
	    configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false); // Important!

	    return new DefaultKafkaProducerFactory<>(configProps);
	}


    @Bean
    public KafkaTemplate<String, AuthorizationResponse> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, AuthorizationRequest> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.chargepoint.authorization.domain");
        
        return new DefaultKafkaConsumerFactory<>(
            configProps,
            new StringDeserializer(),
            new JsonDeserializer<>(AuthorizationRequest.class)
        );
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuthorizationRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuthorizationRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
