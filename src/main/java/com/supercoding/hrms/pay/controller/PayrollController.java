package com.supercoding.hrms.pay.controller;

import com.supercoding.hrms.pay.domain.Payroll;
import com.supercoding.hrms.pay.dto.PayrollCreateRequest;
import com.supercoding.hrms.pay.dto.PayrollSummaryResponse;
import com.supercoding.hrms.pay.dto.PayrollDetailResponse;
import com.supercoding.hrms.pay.service.PayrollService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.supercoding.hrms.pay.domain.PayrollStatus;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    private final PayrollService payrollService;

    // ✅ 생성자 주입 (스프링이 자동으로 PayrollService 주입)
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    /**
     * ✅ [관리자용] 전체 급여 목록 조회
     * - 직원별 급여상태(계산중, 확정, 지급완료) 확인
     * - /api/payrolls
     */
    @GetMapping
    public List<PayrollSummaryResponse> getAllPayrolls() {
        return payrollService.getPayrollSummaries();
    }

    /**
     * ✅ [공통] 특정 급여 상세 조회
     * - 직원 및 관리자가 특정 급여명세서를 열람할 때 사용
     * - /api/payrolls/{id}
     */
    @GetMapping("/{id}")
    public PayrollDetailResponse getPayrollDetail(@PathVariable Long id) {
        return payrollService.getPayrollDetail(id);
    }

    @PutMapping("/{id}/status")
    public void updatePayrollStatus(
            @PathVariable Long id,
            @RequestParam PayrollStatus status
    ) {
        payrollService.updatePayrollStatus(id, status);
    }

    @PostMapping
    public Payroll createPayroll(@RequestBody PayrollCreateRequest request) {
        return payrollService.createPayroll(request);
    }
}
