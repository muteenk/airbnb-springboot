package com.airbnb.payment_service.dtos.responses;

import com.airbnb.payment_service.entities.PaymentRecord;
import com.airbnb.payment_service.entities.PaymentStatus;

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
    public static PaymentResponse from(PaymentRecord record) {
        return new PaymentResponse(
                record.getId(),
                record.getTransactionId(),
                record.getAmountPaid(),
                record.getUserId(),
                record.getBookingId(),
                record.getStatus(),
                record.getCreatedAt()
        );
    }
}
