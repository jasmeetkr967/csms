package com.chargepoint.authorization.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;

import com.chargepoint.authorization.AuthorizationServiceApplication;
import com.chargepoint.authorization.db.domain.DriverAuthorization;
import com.chargepoint.authorization.domain.AuthorizationRequest;
import com.chargepoint.authorization.enums.AuthorizationStatus;
import com.chargepoint.authorization.repo.DriverAuthorizationRepository;

@SpringBootTest(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"},
classes = AuthorizationServiceApplication.class)
//@Import(KafkaTestConfig.class)
public class AuthorizationServiceTest {

    @MockBean
    private KafkaTemplate<String, AuthorizationRequest> kafkaTemplate;
    
    @MockBean
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    @MockBean
    private DriverAuthorizationRepository driverAuthorizationRepository;
    
    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @Test
    void testDetermineAuthorizationStatus_Accepted() {
        String validIdentifier = "valid-id-12345678901234567890";
        DriverAuthorization driverAuthorization = new DriverAuthorization();
        driverAuthorization.setAllowed(true);
        Mockito.when(driverAuthorizationRepository.findByIdentifier(validIdentifier)).thenReturn(Optional.of(driverAuthorization));
        
        AuthorizationStatus status = authorizationService.determineAuthorizationStatus(validIdentifier);
        
        Assertions.assertEquals(AuthorizationStatus.ACCEPTED, status);
    }
    
    @Test
    void testDetermineAuthorizationStatus_Rejected() {
        String validIdentifier = "valid-id-12345678901234567890";
        DriverAuthorization driverAuthorization = new DriverAuthorization();
        driverAuthorization.setAllowed(false);
        Mockito.when(driverAuthorizationRepository.findByIdentifier(validIdentifier)).thenReturn(Optional.of(driverAuthorization));
        
        AuthorizationStatus status = authorizationService.determineAuthorizationStatus(validIdentifier);
        
        Assertions.assertEquals(AuthorizationStatus.REJECTED, status);
    }
    
    @Test
    void testDetermineAuthorizationStatus_Unknown() {
        String validIdentifier = "valid-id-12345678901234567890";
        Mockito.when(driverAuthorizationRepository.findByIdentifier(validIdentifier)).thenReturn(Optional.empty());
        
        AuthorizationStatus status = authorizationService.determineAuthorizationStatus(validIdentifier);
        
        Assertions.assertEquals(AuthorizationStatus.UNKNOWN, status);
    }
    
    @Test
    void testDetermineAuthorizationStatus_Invalid() {
        String invalidIdentifier = "short-id";
        AuthorizationStatus status = authorizationService.determineAuthorizationStatus(invalidIdentifier);
        
        Assertions.assertEquals(AuthorizationStatus.INVALID, status);
    }
}

