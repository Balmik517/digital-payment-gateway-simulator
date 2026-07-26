package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.WebhookPaymentRequest;
import com.balmik.dpgs.service.PaymentService;
import com.balmik.dpgs.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    private final PaymentService paymentService;


    @Override
    public void processPaymentSuccess(WebhookPaymentRequest request) {

        log.info("Webhook received: PAYMENT_SUCCESS, PaymentId={}", request.getPaymentId());

        paymentService.processWebhookSuccess(request.getPaymentId());
    }

    @Override
    public void processPaymentFailed(WebhookPaymentRequest request) {

        log.info("Webhook received: PAYMENT_FAILED, PaymentId={}", request.getPaymentId());

        paymentService.processWebhookFailed(request.getPaymentId());

    }
}
