package com.airbnb.booking_service.repositories;

import com.airbnb.booking_service.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(
            """
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Booking b
            WHERE b.unitId = :unitId
            AND b.bookingFrom < :to
            AND b.bookingTo > :from
            """
    )
    boolean existsOverlapping(
            @Param("unitId") Long unitId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
