package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.request.WebhookPaymentRequest;
import com.balmik.dpgs.service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/payment-success")
    public String paymentSuccess(@RequestBody @Valid WebhookPaymentRequest request){

        webhookService.processPaymentSuccess(request);
        return "Webhook processed";
    }

    @PostMapping("/payment-failed")
    public String paymentFailed(@RequestBody @Valid WebhookPaymentRequest request){

        webhookService.processPaymentFailed(request);
        return "Webhook processed";
    }
}
