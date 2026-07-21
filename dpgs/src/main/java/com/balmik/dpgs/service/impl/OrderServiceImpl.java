package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;
import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.OrderStatus;
import com.balmik.dpgs.exception.OrderNotFoundException;
import com.balmik.dpgs.exception.ResourceAccessDeniedException;
import com.balmik.dpgs.repository.OrderRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(
            CreateOrderRequest request,
            String email) {

        log.info("Order creation initiated by user={}", email);

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

        log.info("Order created successfully. OrderId={}, User={}", order.getOrderId(), email);

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .description(order.getDescription())
                .status(order.getStatus())
                .build();
    }

    @Override
    public OrderResponse getOrderByOrderId(String orderId, String email) {

        log.debug("Fetching order. OrderId={}, User={}", orderId, email);

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("user not found"));

        Order order = orderRepository.findByOrderId(orderId).orElseThrow(()
                -> new OrderNotFoundException("Order not found with that order id."));

        if(!order.getUser().getId().equals(user.getId())){
            throw new ResourceAccessDeniedException("Access denied.");
        }

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .description(order.getDescription())
                .status(order.getStatus())
                .build();
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getOrderId())
                        .amount(order.getAmount())
                        .description(order.getDescription())
                        .status(order.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}