package com.airbnb.booking_service.dtos.responses;

import com.airbnb.booking_service.entities.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String transactionId,
        BigDecimal amountPaid,
        Long userId,
        Long bookingId,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
