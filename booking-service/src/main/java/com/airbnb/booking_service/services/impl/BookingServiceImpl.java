package com.airbnb.booking_service.services.impl;

import com.airbnb.booking_service.clients.PaymentClient;
import com.airbnb.booking_service.clients.PropertyClient;
import com.airbnb.booking_service.dtos.requests.BookingRequest;
import com.airbnb.booking_service.dtos.requests.PaymentRequestBody;
import com.airbnb.booking_service.dtos.responses.BookingResponse;
import com.airbnb.booking_service.dtos.responses.PaymentResponse;
import com.airbnb.booking_service.entities.Booking;
import com.airbnb.booking_service.entities.UnitBookingLock;
import com.airbnb.booking_service.exceptions.ApiException;
import com.airbnb.booking_service.exceptions.ResourceDoesNotExist;
import com.airbnb.booking_service.exceptions.ResourceExistsException;
import com.airbnb.booking_service.payloads.ApiResponse;
import com.airbnb.booking_service.repositories.BookingRepository;
import com.airbnb.booking_service.repositories.UnitBookingLockRepository;
import com.airbnb.booking_service.services.BookingService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UnitBookingLockRepository unitBookingLockRepository;
    private final PropertyClient propertyClient;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    public BookingResponse book(Long userId, BookingRequest bookingRequest) {
        if (!bookingRequest.bookingTo().isAfter(bookingRequest.bookingFrom())) {
            throw new ApiException("bookingTo must be after bookingFrom", HttpStatus.BAD_REQUEST);
        }

        verifyUnitExists(bookingRequest.unitId());

        acquireUnitLock(bookingRequest.unitId());

        if (bookingRepository.existsOverlapping(
                bookingRequest.unitId(),
                bookingRequest.bookingFrom(),
                bookingRequest.bookingTo()
        )) {
            throw new ResourceExistsException("Unit already booking for the selected interval");
        }

        Booking booking = Booking.builder()
                .userId(userId)
                .unitId(bookingRequest.unitId())
                .amount(bookingRequest.amount())
                .bookingFrom(bookingRequest.bookingFrom())
                .bookingTo(bookingRequest.bookingTo())
                .build();

        booking = bookingRepository.save(booking);

        String transactionId = chargePayment(userId, booking);

        return new BookingResponse(booking.getId(), transactionId);
    }

    private void verifyUnitExists(Long unitId) {
        try {
            propertyClient.getUnit(unitId);
        } catch (FeignException.NotFound ex) {
            throw new ResourceDoesNotExist("Unit doesn't exist");
        } catch (FeignException ex) {
            throw new ApiException(
                    "Unable to verify the unit details with property service",
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    private void acquireUnitLock(Long unitId) {
        if (!unitBookingLockRepository.existsById(unitId)){
            try {
                unitBookingLockRepository.saveAndFlush(new UnitBookingLock(unitId));
            } catch (DataIntegrityViolationException ex) {
                throw new ApiException("Data Constraints violated", HttpStatus.CONFLICT);
            }
        }

        unitBookingLockRepository.lockByUnitId(unitId).orElseThrow(
                () -> new ApiException(
                        "Could not acquire booking lock",
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }

    private String chargePayment(Long userId, Booking booking) {
        try {
            ApiResponse<PaymentResponse> response = paymentClient.createPaymentRecord(
                    new PaymentRequestBody(booking.getId(), userId, booking.getAmount())
            );

            if (response.getData() == null || response.getData().transactionId() == null) {
                throw new ApiException("Payment Service returned empty response", HttpStatus.BAD_GATEWAY);
            }
            return response.getData().transactionId();
        } catch (FeignException ex) {
            throw new ApiException("Payment Failed", HttpStatus.BAD_GATEWAY);
        }
    }
}
