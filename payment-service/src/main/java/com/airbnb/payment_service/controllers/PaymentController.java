package com.airbnb.payment_service.controllers;

import com.airbnb.payment_service.dtos.requests.PaymentRequestBody;
import com.airbnb.payment_service.dtos.responses.PaymentResponse;
import com.airbnb.payment_service.payloads.ApiResponse;
import com.airbnb.payment_service.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPaymentRecord(
            @Valid @RequestBody PaymentRequestBody paymentRequestBody
    ) {
        PaymentResponse payment = paymentService.createPayment(paymentRequestBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment recorded", payment));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> listMyPaymentRecords(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Payment records", paymentService.listMyPayments(userId))
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentRecordDetails(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Payment details", paymentService.getPaymentDetails(userId, paymentId))
        );
    }
}
