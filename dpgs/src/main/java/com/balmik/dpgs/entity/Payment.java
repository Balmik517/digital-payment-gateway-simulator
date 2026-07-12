package com.balmik.dpgs.entity;

import com.balmik.dpgs.enums.PaymentMethod;
import com.balmik.dpgs.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments",
indexes = {
        @Index(name = "idx_payment_payment_id", columnList = "paymentId"),
        @Index(name = "idx_payment_order_id", columnList = "order_id")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(unique = true)
    private String transactionReference;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
