package com.airbnb.property_service.dtos.response;

import java.math.BigDecimal;

public record UnitResponse(
        Long id,
        Long propertyId,
        BigDecimal price
) {
}
