package com.balmik.dpgs.service;

import com.balmik.dpgs.entity.IdempotencyRecord;

import java.util.Optional;

public interface IdempotencyService {

    Optional<IdempotencyRecord> findByKey(String key);
    void save(String key, String paymentId);
}
