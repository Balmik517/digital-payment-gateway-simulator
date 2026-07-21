package com.balmik.dpgs.dto.response;

import com.balmik.dpgs.enums.AuditEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAuditResponse {

    private AuditEvent event;
    private String message;
    private String performedBy;
    private LocalDateTime createdAt;
}
