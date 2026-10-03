package com.airbnb.property_service.dtos.request;

import com.airbnb.property_service.entities.PropertyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreatePropertyRequest(
        @NotBlank(message = "Property name is required")
        String propertyName,

        String propertyDescription,

        @NotNull(message = "Property type is required")
        PropertyType propertyType,

        @NotBlank(message = "Address is required")
        String address,

        @NotEmpty(message = "At least one unit is required")
        @Valid
        List<CreateUnitRequest> units
) {
    public record CreateUnitRequest(
            @NotNull(message = "Unit price is required")
            @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
            BigDecimal price
    ) {
    }
}
