package com.airbnb.booking_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRecord {
    private Long id;

    private String transactionId;

    private BigDecimal amountPaid;

    private Long userId;

    private Long bookingId;

    private PaymentStatus status;

    private LocalDateTime createdAt;
}
