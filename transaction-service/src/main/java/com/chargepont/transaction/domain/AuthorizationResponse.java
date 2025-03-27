package com.chargepont.transaction.domain;

import java.io.Serializable;

import com.chargepoint.transaction.enums.AuthorizationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponse implements Serializable {
    private static final long serialVersionUID = -2170442505053278002L;
	private AuthorizationStatus authorizationStatus;
    

}
