package com.supercoding.hrms.pay.schedule;

import com.supercoding.hrms.pay.service.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayrollSchedule {

    private final PayrollService payrollService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void test(){
        payrollService.createPayroll();
    }
}
