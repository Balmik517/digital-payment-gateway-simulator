package com.balmik.dpgs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="idempotency_records", indexes = {@Index(name = "idx_idempotency_key", columnList = "idempotencyKey")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
