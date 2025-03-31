package com.chargepoint.authorization.config;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.chargepoint.authorization.db.domain.DriverAuthorization;
import com.chargepoint.authorization.repo.DriverAuthorizationRepository;

@Configuration
public class DataLoader {

	@Bean
	@Transactional
	ApplicationRunner loadData(DriverAuthorizationRepository driverRepository) {
		return args -> {
			if (driverRepository.count() == 0) { // Prevent duplicate inserts
				driverRepository.saveAll(List.of(new DriverAuthorization("valid-id-12345678901234567890", true),
						new DriverAuthorization("rejected-id-12345678901234567890", false)));
				System.out.println("Initial Data Inserted!");
			} else {
				System.out.println("Data already exists, skipping insert.");
			}
		};
	}
}
