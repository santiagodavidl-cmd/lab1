package com.udea.proyecto.repository;

import com.udea.proyecto.domain.Pasajero;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pasajero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {}
