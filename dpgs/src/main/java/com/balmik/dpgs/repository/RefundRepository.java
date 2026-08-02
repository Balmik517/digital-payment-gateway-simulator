package com.balmik.dpgs.repository;

import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.Refund;
import com.balmik.dpgs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {

    Optional<Refund> findByRefundId(String refundId);

    List<Refund> findByPayment(Payment payment);

    boolean existsByPayment(Payment payment);

    List<Refund> findByPayment_Order_User(User user);
}
