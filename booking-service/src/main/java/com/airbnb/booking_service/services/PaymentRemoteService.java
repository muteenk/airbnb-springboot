package com.airbnb.booking_service.services;

import com.airbnb.booking_service.clients.PaymentClient;
import com.airbnb.booking_service.dtos.requests.PaymentRequestBody;
import com.airbnb.booking_service.dtos.responses.PaymentResponse;
import com.airbnb.booking_service.entities.Booking;
import com.airbnb.booking_service.exceptions.ApiException;
import com.airbnb.booking_service.exceptions.ResourceDoesNotExist;
import com.airbnb.booking_service.payloads.ApiResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentRemoteService {

    private final PaymentClient paymentClient;

    @Retry(name="paymentService")
    @CircuitBreaker(name="paymentService", fallbackMethod = "chargeFallback")
    public String chargePayment(Long userId, Booking booking) {
        ApiResponse<PaymentResponse> response = paymentClient.createPaymentRecord(
                new PaymentRequestBody(booking.getId(), userId, booking.getAmount())
        );

        if (response == null || response.getData() == null || response.getData().transactionId() == null) {
            throw new ApiException("Payment Service returned empty response", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return response.getData().transactionId();
    }

    @SuppressWarnings("unused")
    private String chargeFallback(Long userId, Booking booking, Throwable ex) {
        throw new ApiException(
                "Payment service is unavailable.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

}
