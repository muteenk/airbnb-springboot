package com.airbnb.booking_service.repositories;

import com.airbnb.booking_service.entities.UnitBookingLock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitBookingLockRepository extends JpaRepository<UnitBookingLock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT lock FROM UnitBookingLock lock WHERE lock.unitId = :unitId")
    Optional<UnitBookingLock> lockByUnitId(@Param("unitId") Long unitId);
}
