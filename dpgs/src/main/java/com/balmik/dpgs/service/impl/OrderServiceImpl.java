package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;
import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.OrderStatus;
import com.balmik.dpgs.repository.OrderRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(
            CreateOrderRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = Order.builder()
                .orderId("ORD-" + System.currentTimeMillis())
                .user(user)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .description(order.getDescription())
                .status(order.getStatus())
                .build();
    }
}