package com.chargepoint.authorization.db.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "driver_authorizations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverAuthorization {

	@Id
    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(nullable = false)
    private boolean allowed;

    

}
