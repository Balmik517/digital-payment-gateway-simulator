package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.InitiatePaymentRequest;
import com.balmik.dpgs.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse initiatePayment(InitiatePaymentRequest request, String email);

    PaymentResponse markSuccess(String paymentId, String email);

    PaymentResponse markFailed(String paymentId, String email);

    PaymentResponse getPayment(String paymentId, String email);

    List<PaymentResponse> getPaymentsByOrder(String orderId, String email);
}
