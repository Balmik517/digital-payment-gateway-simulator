package com.balmik.dpgs.repository;

import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.PaymentAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentAuditRepository extends JpaRepository<PaymentAudit, UUID> {

    List<PaymentAudit> findByPaymentOrderByCreatedAtAsc(Payment payment);
}
