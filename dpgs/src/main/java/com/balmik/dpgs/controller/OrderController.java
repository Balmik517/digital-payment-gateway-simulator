package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;
import com.balmik.dpgs.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("createOrder")
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request, Authentication authentication){
        return orderService.createOrder(request, authentication.getName());
    }
}
