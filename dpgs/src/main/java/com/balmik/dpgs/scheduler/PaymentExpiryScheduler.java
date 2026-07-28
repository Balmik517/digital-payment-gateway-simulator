package com.balmik.dpgs.scheduler;

import com.balmik.dpgs.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentExpiryScheduler {

    private final PaymentService paymentService;

    @Scheduled(fixedDelayString  = "${payment.scheduler.delay}")
    public void expirePendingPayments(){
        log.info("Running payment expiry scheduler...");
        paymentService.expirePendingPayments();
    }
}
