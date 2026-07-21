package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.PaymentAudit;
import com.balmik.dpgs.enums.AuditEvent;
import com.balmik.dpgs.repository.PaymentAuditRepository;
import com.balmik.dpgs.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final PaymentAuditRepository paymentAuditRepository;

    @Override
    public void saveAudit(Payment payment, AuditEvent event, String message, String performedBy) {

        PaymentAudit audit = PaymentAudit.builder()
                .payment(payment).event(event).message(message).createdAt(LocalDateTime.now()).performedBy(performedBy).build();

        paymentAuditRepository.save(audit);
    }
}
