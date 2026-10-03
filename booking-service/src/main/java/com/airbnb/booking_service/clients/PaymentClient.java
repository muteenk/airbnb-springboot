package com.airbnb.booking_service.clients;

import com.airbnb.booking_service.dtos.requests.PaymentRequestBody;
import com.airbnb.booking_service.dtos.responses.PaymentResponse;
import com.airbnb.booking_service.payloads.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/v1/payments")
    ApiResponse<PaymentResponse> createPaymentRecord(
            @RequestBody PaymentRequestBody paymentRequestBody
    );
}
