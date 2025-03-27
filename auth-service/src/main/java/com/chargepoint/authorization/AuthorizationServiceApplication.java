package com.chargepoint.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chargepoint.authorization", "com.chargepoint.authorization.consumer"})
public class AuthorizationServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(AuthorizationServiceApplication.class, args);
    }
}
