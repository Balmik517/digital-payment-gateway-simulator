package com.balmik.dpgs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WebhookPaymentRequest {

    @NotBlank
    private String paymentId;
}
