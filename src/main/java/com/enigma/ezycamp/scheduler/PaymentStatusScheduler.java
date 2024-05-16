package com.enigma.ezycamp.scheduler;

import com.enigma.ezycamp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusScheduler {
    private final PaymentService paymentService;

    @Scheduled(fixedRate = 30000)
    public void checkFailedPayments(){
        log.info("START checking failed payments : {}", System.currentTimeMillis());
        paymentService.checkFailedAndUpdateStatus();
        log.info("END checking failed payments : {}", System.currentTimeMillis());
    }
}
