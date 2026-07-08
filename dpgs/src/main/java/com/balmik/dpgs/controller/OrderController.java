package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.request.CreateOrderRequest;
import com.balmik.dpgs.dto.response.OrderResponse;
import com.balmik.dpgs.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("createOrder")
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request, Authentication authentication){
        return orderService.createOrder(request, authentication.getName());
    }

    @GetMapping("getOrder/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId, Authentication authentication){
        return orderService.getOrderByOrderId(orderId, authentication.getName());
    }

    @GetMapping("getMyOrders")
    public List<OrderResponse> getMyOrders(Authentication authentication){
       return orderService.getMyOrders(authentication.getName());
    }


}
