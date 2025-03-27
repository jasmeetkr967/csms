package com.chargepoint.transaction.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.chargepoint.transaction.enums.AuthorizationStatus;
import com.chargepont.transaction.domain.AuthorizationResponse;

@Service
public class KafkaConsumerService {

	private final Map<String, CompletableFuture<AuthorizationResponse>> pendingRequests = new ConcurrentHashMap<>();

	
	/**
	 * This method is there to read the response from Authorization Service
	 * @param requestId
	 * @param response
	 */
	@KafkaListener(topics = "authorization-responses", groupId = "auth-group")
	public void processResponse(@Header(KafkaHeaders.RECEIVED_KEY) String requestId, AuthorizationResponse response) {
		CompletableFuture<AuthorizationResponse> future = pendingRequests.remove(requestId);
		if (future != null) {
			future.complete(response);
		}
	}

	
	/**
	 * This method is to wait for 5 seconds to receive the response
	 * @param requestId
	 * @return
	 */
	public AuthorizationResponse getAuthorizationResponse(String requestId) {
		CompletableFuture<AuthorizationResponse> future = new CompletableFuture<>();
		pendingRequests.put(requestId, future);
		try {
			return future.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			return new AuthorizationResponse(AuthorizationStatus.UNKNOWN);
		}
	}

}
