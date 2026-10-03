package com.airbnb.property_service.repositories;

import com.airbnb.property_service.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByOwnerId(Long ownerId);

    Optional<Property> findByIdAndOwnerId(Long id, Long ownerId);
}
