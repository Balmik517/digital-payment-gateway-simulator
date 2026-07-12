package com.balmik.dpgs.repository;

import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByPaymentId(String paymentId);

    List<Payment> findByOrder(Order order);
}
