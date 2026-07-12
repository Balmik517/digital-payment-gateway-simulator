package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.request.InitiatePaymentRequest;
import com.balmik.dpgs.dto.response.PaymentResponse;
import com.balmik.dpgs.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("initiatePayment")
    public PaymentResponse initiatePayment(@RequestBody @Valid InitiatePaymentRequest request, Authentication authentication){
     return paymentService.initiatePayment(request, authentication.getName());
    }

    @PostMapping("/{paymentId}/success")
    public PaymentResponse markSuccess(@PathVariable String paymentId, Authentication authentication) {

        return paymentService.markSuccess(paymentId, authentication.getName());
    }

    @PostMapping("/{paymentId}/fail")
    public PaymentResponse markFailed(@PathVariable String paymentId, Authentication authentication) {

        return paymentService.markFailed(paymentId, authentication.getName());
    }
}
