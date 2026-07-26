package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.WebhookPaymentRequest;

public interface WebhookService {

    void processPaymentSuccess(WebhookPaymentRequest request);
    void processPaymentFailed(WebhookPaymentRequest request);
}
