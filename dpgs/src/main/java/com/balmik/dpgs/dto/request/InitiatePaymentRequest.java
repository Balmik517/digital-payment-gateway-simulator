package com.balmik.dpgs.dto.request;

import com.balmik.dpgs.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitiatePaymentRequest {

    @NotBlank
    private String orderId;

    @NotNull
    private PaymentMethod paymentMethod;
}
