package com.balmik.dpgs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRefundRequest {

    @NotBlank
    private String paymentId;

    @NotBlank
    private String reason;

}
