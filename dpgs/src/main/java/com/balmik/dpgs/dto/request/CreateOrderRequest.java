package com.balmik.dpgs.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal amount;

    private String description;
}
