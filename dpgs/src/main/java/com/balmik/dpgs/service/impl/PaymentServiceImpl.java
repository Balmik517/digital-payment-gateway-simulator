package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.InitiatePaymentRequest;
import com.balmik.dpgs.dto.response.PaymentResponse;
import com.balmik.dpgs.entity.Notification;
import com.balmik.dpgs.entity.Order;
import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.NotificationStatus;
import com.balmik.dpgs.enums.NotificationType;
import com.balmik.dpgs.enums.OrderStatus;
import com.balmik.dpgs.enums.PaymentStatus;
import com.balmik.dpgs.exception.*;
import com.balmik.dpgs.repository.NotificationRepository;
import com.balmik.dpgs.repository.OrderRepository;
import com.balmik.dpgs.repository.PaymentRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    @Override
    @Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest request, String email) {

        log.info("Payment initiation started. OrderId={}, User={}", request.getOrderId(), email);

        User user = getCurrentUser(email);

        Order order = orderRepository.findByOrderId(request.getOrderId()).orElseThrow(
                () -> new OrderNotFoundException("Order not found"));

        if(order.getStatus() == OrderStatus.PAID){
            throw new PaymentAlreadyProcessedException("Order already paid");
        }

        if (!order.getUser().getId().equals(user.getId())) {
            log.warn("Unauthorized payment attempt. User={}, Order={}", email, order.getOrderId());

            throw new ResourceAccessDeniedException("You cannot pay for another user's order");
        }

        paymentRepository.findByOrderAndStatus(order, PaymentStatus.PENDING).ifPresent(payment -> {
            throw new PaymentAlreadyExistsException("Payment already in progress");});

        order.setStatus(OrderStatus.PAYMENT_PENDING);

        Payment payment = Payment.builder().paymentId("PAY-"+System.currentTimeMillis()).order(order).amount(order.getAmount())
                .paymentMethod(request.getPaymentMethod()).status(PaymentStatus.PENDING)
                .transactionReference("TNX-"+System.currentTimeMillis()).createdAt(LocalDateTime.now()).build();

        orderRepository.save(order);
        paymentRepository.save(payment);

        log.info("Payment initiated successfully. PaymentId={}, OrderId={}", payment.getPaymentId(), order.getOrderId());

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId()).amount(order.getAmount())
                .paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    @Transactional
    public PaymentResponse markSuccess(String paymentId, String email) {

        log.info("Processing payment success. PaymentId={}", paymentId);

        User user = getCurrentUser(email);

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found"));

        if (!payment.getOrder().getUser().getId().equals(user.getId())) {
            throw new ResourceAccessDeniedException("You cannot pay for another user's order");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentAlreadyProcessedException("Payment has already been processed");
        }

        payment.setStatus(PaymentStatus.SUCCESS);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);

        Notification notification = Notification.builder().user(order.getUser())
                .type(NotificationType.EMAIL).status(NotificationStatus.SENT)
                .subject("Payment Successful")
                .message("Your payment "
                +payment.getPaymentId()+
                        " for order "+
                        order.getOrderId()+
                        " was completed successfully.")
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        log.info("Notification created. User={}, Type={}, Subject={}", order.getUser().getEmail(), notification.getType(),
                notification.getSubject());

        orderRepository.save(order);
        paymentRepository.save(payment);


        log.info("Payment marked SUCCESS. PaymentId={}, OrderId={}", payment.getPaymentId(), order.getOrderId());

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    @Transactional
    public PaymentResponse markFailed(String paymentId, String email) {

        log.info("Processing payment fail. PaymentId={}", paymentId);

        User user = getCurrentUser(email);

        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found"));

        if (!payment.getOrder().getUser().getId().equals(user.getId())) {
            throw new ResourceAccessDeniedException("You cannot pay for another user's order");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentAlreadyProcessedException("Payment has already been processed");
        }

        payment.setStatus(PaymentStatus.FAILED);
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.FAILED);

        orderRepository.save(order);
        paymentRepository.save(payment);


        log.warn("Payment marked FAILED. PaymentId={}, OrderId={}", payment.getPaymentId(), order.getOrderId());

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    public PaymentResponse getPayment(String paymentId, String email) {


        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(
                ()-> new PaymentNotFoundException("Payment not found"));

        validateOwnership(payment.getOrder(), email);

        return PaymentResponse.builder().paymentId(payment.getPaymentId()).orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod()).status(payment.getStatus())
                .transactionReference(payment.getTransactionReference()).build();
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(String orderId, String email) {

        Order order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order not found"));

        validateOwnership(order, email);

        return paymentRepository.findByOrder(order).stream().map(this::mapToResponse).toList();
    }

    private User getCurrentUser(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found"));
    }


    private void validateOwnership(Order order, String email){
        User user = getCurrentUser(email);

        if(!order.getUser().getId().equals(user.getId())){
            throw new ResourceAccessDeniedException("Access denied");
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
