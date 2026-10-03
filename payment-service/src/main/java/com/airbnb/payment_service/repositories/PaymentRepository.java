package com.airbnb.payment_service.repositories;

import com.airbnb.payment_service.entities.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentRecord, Long> {
    List<PaymentRecord> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<PaymentRecord> findByIdAndUserId(Long id, Long userId);

    boolean existsByBookingId(Long bookingId);
}
