package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.InitiatePaymentRequest;
import com.balmik.dpgs.dto.response.PaymentResponse;
import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.OrderStatus;
import com.balmik.dpgs.enums.PaymentStatus;
import com.balmik.dpgs.repository.OrderRepository;
import com.balmik.dpgs.repository.PaymentRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderRepository.findByOrderId(request.getOrderId()).orElseThrow(
                () -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot pay for another user's order");
        }

        order.setStatus(OrderStatus.PAYMENT_PENDING);

        Payment payment = Payment.builder().paymentId("PAY-"+System.currentTimeMillis()).order(order).amount(order.getAmount())
                .paymentMethod(request.getPaymentMethod()).status(PaymentStatus.PENDING)
                .transactionReference("TNX-"+System.currentTimeMillis()).createdAt(LocalDateTime.now()).build();

        paymentRepository.save(payment);
        orderRepository.save(order);
        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId()).amount(order.getAmount())
                .paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    @Transactional
    public PaymentResponse markSuccess(String paymentId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new RuntimeException("Payment not found"));

        if (!payment.getOrder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot pay for another user's order");
        }

        payment.setStatus(PaymentStatus.SUCCESS);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    @Transactional
    public PaymentResponse markFailed(String paymentId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new RuntimeException("Payment not found"));

        if (!payment.getOrder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot pay for another user's order");
        }

        payment.setStatus(PaymentStatus.FAILED);
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.FAILED);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    public PaymentResponse getPayment(String paymentId, String email) {


        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                ()-> new RuntimeException("Payment not found"));

        validateOwnership(payment.getOrder(), email);

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(String orderId, String email) {

        Order order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new RuntimeException("Order not found"));

        validateOwnership(order, email);

        return paymentRepository.findByOrder(order).stream().map(this::mapToResponse).toList();
    }


    private void validateOwnership(Order order, String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));

        if(!order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .build();
    }
}
