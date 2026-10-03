package com.airbnb.property_service.repositories;

import com.airbnb.property_service.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
