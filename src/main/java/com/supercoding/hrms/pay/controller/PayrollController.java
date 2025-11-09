package com.supercoding.hrms.pay.controller;

import com.supercoding.hrms.pay.dto.PayrollSummaryResponse;
import com.supercoding.hrms.pay.dto.PayrollDetailResponse;
import com.supercoding.hrms.pay.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor  // ✅ 생성자 자동 주입
public class PayrollController {

    private final PayrollService payrollService;

    /**
     * ✅ [관리자용] 전체 급여 목록 조회
     * 예시: GET http://localhost:8080/api/payrolls
     */

    @GetMapping("")
    public ResponseEntity<List<PayrollSummaryResponse>> getPayrolls(){
        return ResponseEntity.ok(payrollService.getPayrollSummaries());
    }

    /**
     * ✅ 급여 상세 조회 (직원/관리자 공통)
     * 예시: GET http://localhost:8080/api/payrolls/1
     */
    @GetMapping("/{id}")
    public PayrollDetailResponse getPayrollDetail(@PathVariable Long id) {
        return payrollService.getPayrollDetail(id);
    }
}