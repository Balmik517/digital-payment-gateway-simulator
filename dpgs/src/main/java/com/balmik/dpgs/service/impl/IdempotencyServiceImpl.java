package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.entity.IdempotencyRecord;
import com.balmik.dpgs.repository.IdempotencyRepository;
import com.balmik.dpgs.service.IdempotencyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Override
    public Optional<IdempotencyRecord> findByKey(String key) {
        return idempotencyRepository.findByIdempotencyKey(key);
    }

    @Override
    public void save(String key, String paymentId) {

        idempotencyRepository.save(IdempotencyRecord.builder().idempotencyKey(key).paymentId(paymentId)
                .createdAt(LocalDateTime.now()).build());

    }
}
