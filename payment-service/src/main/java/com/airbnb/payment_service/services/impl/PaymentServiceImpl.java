package com.airbnb.payment_service.services.impl;

import com.airbnb.payment_service.dtos.requests.PaymentRequestBody;
import com.airbnb.payment_service.dtos.responses.PaymentResponse;
import com.airbnb.payment_service.entities.PaymentRecord;
import com.airbnb.payment_service.entities.PaymentStatus;
import com.airbnb.payment_service.exceptions.ResourceDoesNotExist;
import com.airbnb.payment_service.exceptions.ResourceExistsException;
import com.airbnb.payment_service.repositories.PaymentRepository;
import com.airbnb.payment_service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequestBody request) {
        if (paymentRepository.existsByBookingId(request.bookingId())) {
            throw new ResourceExistsException("Payment already exists for this booking");
        }

        PaymentRecord paymentRecord = PaymentRecord.builder()
                .transactionId(UUID.randomUUID().toString())
                .amountPaid(request.amountPaid())
                .userId(request.userId())
                .bookingId(request.bookingId())
                .status(PaymentStatus.SUCCESS)
                .build();

        return PaymentResponse.from(paymentRepository.save(paymentRecord));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> listMyPayments(Long userId) {
        return paymentRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentDetails(Long userId, Long paymentId) {
        return paymentRepository.findByIdAndUserId(paymentId, userId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new ResourceDoesNotExist("Payment not found"));
    }
}
