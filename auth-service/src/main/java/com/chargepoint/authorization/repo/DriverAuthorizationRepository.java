package com.chargepoint.authorization.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chargepoint.authorization.db.domain.DriverAuthorization;

@Repository
public interface DriverAuthorizationRepository extends JpaRepository<DriverAuthorization, UUID> {
   
	Optional<DriverAuthorization> findByIdentifier(String identifier);
}
