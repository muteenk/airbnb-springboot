package com.airbnb.booking_service.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingRequest(
        @NotNull
        Long unitId,

        @NotNull
        BigDecimal amount,

        @NotNull
        LocalDateTime bookingFrom,

        @NotNull
        LocalDateTime bookingTo
) {
}
