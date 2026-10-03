package com.airbnb.payment_service.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequestBody(
        @NotNull(message = "Booking id is required")
        Long bookingId,

        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        BigDecimal amountPaid
) {
}
