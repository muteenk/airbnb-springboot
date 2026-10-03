package com.airbnb.booking_service.services;

import com.airbnb.booking_service.dtos.requests.BookingRequest;
import com.airbnb.booking_service.dtos.responses.BookingResponse;
import jakarta.validation.Valid;

public interface BookingService {
    BookingResponse book(Long userId, @Valid BookingRequest bookingRequest);
}
