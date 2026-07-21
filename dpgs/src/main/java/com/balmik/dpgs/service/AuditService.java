package com.balmik.dpgs.service;

import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.enums.AuditEvent;

public interface AuditService {
    void saveAudit(Payment payment, AuditEvent event, String message, String performedBy);
}
