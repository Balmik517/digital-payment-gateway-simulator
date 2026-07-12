package com.balmik.dpgs.dto.response;

import com.balmik.dpgs.enums.PaymentMethod;
import com.balmik.dpgs.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private String paymentId;

    private String orderId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String transactionReference;

}
