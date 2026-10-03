package com.airbnb.booking_service.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequestBody(
        Long bookingId,
        Long userId,
        BigDecimal amountPaid
) {
}
