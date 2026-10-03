package com.airbnb.property_service.services.impl;

import com.airbnb.property_service.dtos.request.CreatePropertyRequest;
import com.airbnb.property_service.dtos.response.UnitResponse;
import com.airbnb.property_service.entities.Property;
import com.airbnb.property_service.entities.Unit;
import com.airbnb.property_service.exceptions.ResourceDoesNotExist;
import com.airbnb.property_service.repositories.PropertyRepository;
import com.airbnb.property_service.repositories.UnitRepository;
import com.airbnb.property_service.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public Property createProperty(Long ownerId, CreatePropertyRequest request) {
        Property property = Property.builder()
                .propertyName(request.propertyName())
                .propertyDescription(request.propertyDescription())
                .propertyType(request.propertyType())
                .address(request.address())
                .ownerId(ownerId)
                .build();

        Set<Unit> units = new HashSet<>();
        for (CreatePropertyRequest.CreateUnitRequest unitRequest : request.units()) {
            Unit unit = Unit.builder()
                    .price(unitRequest.price())
                    .property(property)
                    .build();
            units.add(unit);
        }
        property.setUnits(units);

        return propertyRepository.save(property);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Property> listOwnedProperties(Long ownerId) {
        return propertyRepository.findAllByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Property getOwnedProperty(Long ownerId, Long propertyId) {
        return propertyRepository.findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new ResourceDoesNotExist("Property not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Property> listPublicProperties() {
        return propertyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Property getPublicProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceDoesNotExist("Property not found"));
    }

    @Override
    public UnitResponse getUnitById(Long unitId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResourceDoesNotExist("Unit doesn't exist"));

        return new UnitResponse(unit.getId(), unit.getProperty().getId(), unit.getPrice());
    }
}
