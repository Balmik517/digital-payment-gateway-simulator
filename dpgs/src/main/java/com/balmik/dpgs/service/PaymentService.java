package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.InitiatePaymentRequest;
import com.balmik.dpgs.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse initiatePayment(InitiatePaymentRequest request, String email);

    PaymentResponse markSuccess(String paymentId, String email);

    PaymentResponse markFailed(String paymentId, String email);
}
