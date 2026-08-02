package com.balmik.dpgs.dto.response;

import com.balmik.dpgs.enums.RefundStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RefundResponse {

    private String refundId;

    private String paymentId;

    private BigDecimal amount;

    private RefundStatus status;

    private String reason;
}
