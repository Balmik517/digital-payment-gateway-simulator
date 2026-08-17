package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.CreateRefundRequest;
import com.balmik.dpgs.dto.response.RefundResponse;
import com.balmik.dpgs.entity.Notification;
import com.balmik.dpgs.entity.Payment;
import com.balmik.dpgs.entity.Refund;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.*;
import com.balmik.dpgs.exception.*;
import com.balmik.dpgs.repository.NotificationRepository;
import com.balmik.dpgs.repository.PaymentRepository;
import com.balmik.dpgs.repository.RefundRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.AuditService;
import com.balmik.dpgs.service.RefundService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundServiceImpl implements RefundService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final AuditService auditService;

    @Override
    @Transactional
    public RefundResponse createRefund(CreateRefundRequest request, String email) {
        log.info("Refund request received. PaymentId={}, User={}", request.getPaymentId(), email);

        User user = getCurrentUser(email);

        Payment payment = paymentRepository.findByPaymentId(request.getPaymentId()).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found"));


        if (!payment.getOrder().getUser().getId().equals(user.getId())) {

            log.warn("Unauthorized refund attempt. User={}, Payment={}", email, payment.getPaymentId());

            throw new ResourceAccessDeniedException("You cannot refund another user's payment");
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {

            throw new IllegalStateException("Only successful payments can be refunded");
        }

        if (refundRepository.existsByPayment(payment)) {

            throw new RefundAlreadyExistsException("Refund already exists for this payment");
        }

        Refund refund = Refund.builder()
                .refundId("REF-" + System.currentTimeMillis())
                .payment(payment)
                .amount(payment.getAmount())
                .status(RefundStatus.INITIATED)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .build();

        refundRepository.save(refund);

        log.info("Refund created successfully. RefundId={}",
                refund.getRefundId());

        createNotification(refund);

        auditService.saveAudit(
                payment,
                AuditEvent.REFUND_CREATED,
                "Refund initiated. RefundId=" + refund.getRefundId(),
                email
        );

        return mapToResponse(refund);
    }

    @Override
    public RefundResponse getRefund(String refundId, String email) {
        Refund refund = refundRepository
                .findByRefundId(refundId)
                .orElseThrow(() -> new RefundNotFoundException("Refund not found"));

        User user = getCurrentUser(email);

        if (!refund.getPayment().getOrder().getUser().getId().equals(user.getId())) {

            throw new ResourceAccessDeniedException("Access denied");
        }

        return mapToResponse(refund);
    }

    @Override
    public List<RefundResponse> getMyRefunds(String email) {
        User user = getCurrentUser(email);

        return refundRepository
                .findByPayment_Order_User(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public RefundResponse approveRefund(String refundId) {

        Refund refund = refundRepository.findByRefundId(refundId).orElseThrow(() ->
              new RefundNotFoundException("Refund not found"));

        if(refund.getStatus() != RefundStatus.INITIATED){
            throw new IllegalStateException("Refund already processed");
        }

        refund.setStatus(RefundStatus.SUCCESS);
        refundRepository.save(refund);

        createRefundProcessedNotification(refund, "Refund Approved",
                "Refund " + refund.getRefundId() + " has been approved.");

        auditService.saveAudit(refund.getPayment(), AuditEvent.REFUND_SUCCESS, "Refund approved. RefundId=" + refundId,
                "ADMIN");

        auditService.saveAudit(refund.getPayment(), AuditEvent.NOTIFICATION_SENT, "Refund notification generated",
                "SYSTEM");

        log.info("Refund approved. RefundId={}", refundId);

        return mapToResponse(refund);
    }

    @Override
    @Transactional
    public RefundResponse rejectRefund(String refundId) {

        Refund refund = refundRepository.findByRefundId(refundId)
                .orElseThrow(() -> new RefundNotFoundException("Refund not found"));

        if(refund.getStatus() != RefundStatus.INITIATED){
            throw new IllegalStateException("Refund already processed");
        }

        refund.setStatus(RefundStatus.FAILED);

        refundRepository.save(refund);

        createRefundProcessedNotification(refund, "Refund Rejected",
                "Refund " + refund.getRefundId() + " has been rejected.");

        auditService.saveAudit(refund.getPayment(), AuditEvent.REFUND_FAILED, "Refund rejected. RefundId=" + refundId,
                "ADMIN");

        log.info("Refund rejected. RefundId={}", refundId);

        return mapToResponse(refund);
    }


    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    private RefundResponse mapToResponse(Refund refund) {

        return RefundResponse.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPayment().getPaymentId())
                .amount(refund.getAmount())
                .status(refund.getStatus())
                .reason(refund.getReason())
                .build();
    }

    private void createNotification(Refund refund) {

        Notification notification = Notification.builder()
                .user(refund.getPayment().getOrder().getUser())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.SENT)
                .subject("Refund Initiated")
                .message("Refund " +
                        refund.getRefundId() +
                        " has been initiated for payment " +
                        refund.getPayment().getPaymentId())
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        log.info("Refund notification created. RefundId={}", refund.getRefundId());
    }


    private void createRefundProcessedNotification(Refund refund, String subject, String message) {

        Notification notification = Notification.builder()
                .user(refund.getPayment().getOrder().getUser())
                .type(NotificationType.EMAIL)
                .status(NotificationStatus.SENT)
                .subject(subject)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
}
