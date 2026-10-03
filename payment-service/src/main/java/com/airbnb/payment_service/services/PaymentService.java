package com.airbnb.payment_service.services;

import com.airbnb.payment_service.dtos.requests.PaymentRequestBody;
import com.airbnb.payment_service.dtos.responses.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequestBody request);

    List<PaymentResponse> listMyPayments(Long userId);

    PaymentResponse getPaymentDetails(Long userId, Long paymentId);
}
