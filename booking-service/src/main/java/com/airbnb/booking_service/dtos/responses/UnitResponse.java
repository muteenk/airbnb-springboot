package com.airbnb.booking_service.dtos.responses;

import java.math.BigDecimal;

public record UnitResponse(
        Long id,
        Long propertyId,
        BigDecimal price
) {
}
