package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request, String email);

    OrderResponse getOrderByOrderId(String orderId, String email);

    List<OrderResponse> getMyOrders(String email);
}
