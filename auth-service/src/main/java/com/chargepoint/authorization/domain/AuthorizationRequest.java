package com.chargepoint.authorization.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationRequest implements Serializable {
	private static final long serialVersionUID = -5400058976544800507L;
	private String stationUuid;
    private DriverIdentifier driverIdentifier;
	
    

}
