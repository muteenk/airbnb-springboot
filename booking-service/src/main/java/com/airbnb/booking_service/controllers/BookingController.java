package com.airbnb.booking_service.controllers;

import com.airbnb.booking_service.dtos.requests.BookingRequest;
import com.airbnb.booking_service.dtos.responses.BookingResponse;
import com.airbnb.booking_service.payloads.ApiResponse;
import com.airbnb.booking_service.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> bookProperty(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid BookingRequest bookingRequest
    ) {
        BookingResponse booking = bookingService.book(userId, bookingRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking Created", booking));
    }

}
