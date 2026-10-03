package com.airbnb.property_service.controllers;

import com.airbnb.property_service.dtos.response.UnitResponse;
import com.airbnb.property_service.payloads.ApiResponse;
import com.airbnb.property_service.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final PropertyService propertyService;

    @GetMapping("/{unitId}")
    public ResponseEntity<ApiResponse<UnitResponse>> getById(@PathVariable("unitId") Long unitId) {
        return ResponseEntity.ok(
                ApiResponse.success("Unit Fetched", propertyService.getUnitById(unitId))
        );
    }

}
