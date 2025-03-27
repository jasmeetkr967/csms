package com.chargepoint.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chargepoint.transaction.enums.AuthorizationStatus;
import com.chargepoint.transaction.service.KafkaConsumerService;
import com.chargepont.transaction.domain.AuthorizationRequest;
import com.chargepont.transaction.domain.AuthorizationResponse;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	private final KafkaTemplate<String, AuthorizationRequest> kafkaTemplate;
    private final KafkaConsumerService kafkaConsumerService;

    public TransactionController(KafkaTemplate<String, AuthorizationRequest> kafkaTemplate,
                                 KafkaConsumerService kafkaConsumerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    
    /**
     * Method to receive request and return response
     * @param request
     * @return
     */
    @PostMapping("/authorize")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest request) {
        if (request.getDriverIdentifier().getId().length() < 20 || request.getDriverIdentifier().getId().length() > 80) {
            return ResponseEntity.badRequest().body(new AuthorizationResponse(AuthorizationStatus.INVALID));
        }
        kafkaTemplate.send("authorization-requests", request.getDriverIdentifier().getId(), request);
        AuthorizationResponse response = kafkaConsumerService.getAuthorizationResponse(request.getDriverIdentifier().getId());
        return ResponseEntity.ok(response);
        
    }
	
}
