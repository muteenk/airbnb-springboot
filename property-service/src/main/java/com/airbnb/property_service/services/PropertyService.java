package com.airbnb.property_service.services;

import com.airbnb.property_service.dtos.request.CreatePropertyRequest;
import com.airbnb.property_service.dtos.response.UnitResponse;
import com.airbnb.property_service.entities.Property;

import java.util.List;

public interface PropertyService {
    Property createProperty(Long ownerId, CreatePropertyRequest request);

    List<Property> listOwnedProperties(Long ownerId);

    Property getOwnedProperty(Long ownerId, Long propertyId);

    List<Property> listPublicProperties();

    Property getPublicProperty(Long propertyId);

    UnitResponse getUnitById(Long unitId);
}
