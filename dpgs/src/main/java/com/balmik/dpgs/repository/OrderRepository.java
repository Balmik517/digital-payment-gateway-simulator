package com.balmik.dpgs.repository;

import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByUser(User user);


}
