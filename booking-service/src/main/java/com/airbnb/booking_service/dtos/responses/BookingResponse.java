package com.airbnb.booking_service.dtos.responses;

public record BookingResponse(
        Long bookingId,
        String paymentTransactionId
) {
}
