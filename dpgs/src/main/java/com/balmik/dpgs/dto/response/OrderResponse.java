package com.balmik.dpgs.dto.response;

import com.balmik.dpgs.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String orderId;

    private BigDecimal amount;

    private String description;

    private OrderStatus status;
}
