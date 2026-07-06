package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request, String email);
}
