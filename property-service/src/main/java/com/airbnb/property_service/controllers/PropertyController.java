package com.airbnb.property_service.controllers;

import com.airbnb.property_service.dtos.request.CreatePropertyRequest;
import com.airbnb.property_service.entities.Property;
import com.airbnb.property_service.payloads.ApiResponse;
import com.airbnb.property_service.services.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Property>> createNewProperty(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreatePropertyRequest request
    ) {
        Property property = propertyService.createProperty(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Property created", property));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Property>>> listOwnedProperties(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Owned properties", propertyService.listOwnedProperties(userId))
        );
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<ApiResponse<Property>> getOwnedPropertyDetails(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long propertyId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Property details", propertyService.getOwnedProperty(userId, propertyId))
        );
    }

    @GetMapping("/public/list")
    public ResponseEntity<ApiResponse<List<Property>>> listPropertiesPublic() {
        return ResponseEntity.ok(
                ApiResponse.success("Properties", propertyService.listPublicProperties())
        );
    }

    @GetMapping("/public/list/{propertyId}")
    public ResponseEntity<ApiResponse<Property>> getPropertyPublic(@PathVariable Long propertyId) {
        return ResponseEntity.ok(
                ApiResponse.success("Property details", propertyService.getPublicProperty(propertyId))
        );
    }
}
