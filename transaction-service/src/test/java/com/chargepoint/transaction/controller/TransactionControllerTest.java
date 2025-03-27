package com.chargepoint.transaction.controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.chargepoint.transaction.enums.AuthorizationStatus;
import com.chargepoint.transaction.service.KafkaConsumerService;
import com.chargepont.transaction.domain.AuthorizationRequest;
import com.chargepont.transaction.domain.AuthorizationResponse;
import com.chargepont.transaction.domain.DriverIdentifier;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
@ExtendWith(SpringExtension.class)
//@Import(TransactionController.class)  // Explicitly import if needed
public class TransactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private KafkaTemplate<String, AuthorizationRequest> kafkaTemplate;
    
    @MockBean
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void testAuthorizeValidRequest() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest(new DriverIdentifier("valid-id-12345678901234567890"));
        AuthorizationResponse response = new AuthorizationResponse(AuthorizationStatus.ACCEPTED);
        
        Mockito.when(kafkaConsumerService.getAuthorizationResponse(Mockito.anyString())).thenReturn(response);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorizationStatus").value(AuthorizationStatus.ACCEPTED.name()));
    }

    @Test
    void testAuthorizeInvalidRequest() throws Exception {
        AuthorizationRequest request = new AuthorizationRequest(new DriverIdentifier("short-id"));
        
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorizationStatus").value(AuthorizationStatus.INVALID.name()));
    }
}
