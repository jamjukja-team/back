package com.supercoding.hrms.pay.controller;

import com.supercoding.hrms.pay.dto.PayrollType;
import com.supercoding.hrms.pay.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor //생성자 자동 주입
public class PayrollController {

    private final PayrollService payrollService;

    //C (Create)
    //급여 이력 생성
    @PostMapping
    public ResponseEntity<PayrollType> createPayroll(@RequestBody PayrollType request) {
        PayrollType response = payrollService.createPayroll(request);
        return ResponseEntity.ok(response);
    }

    //R(L) (Read List)
    //전체 급여 목록 조회 (관리자용)
    @GetMapping("/list")
    public ResponseEntity<List<PayrollSummaryResponse>> getPayrolls() {
        return ResponseEntity.ok(payrollService.getPayrolls());
    }

    //R (Read)
    //특정 급여 이력 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PayrollDetailResponse> getPayroll(@PathVariable Long id) {
        PayrollDetailResponse response = payrollService.getPayroll(id);
        return ResponseEntity.ok(response);
    }

    //U (Update)
    //급여 이력 수정 (예: 상태, 날짜 등)
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updatePayroll(
            @PathVariable Long id,
            @RequestBody PayrollDetailResponse request) {
        try {
            payrollService.updatePayroll(id, request);
            return ResponseEntity.ok(true);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    //D (Delete)
    //단일 급여 이력 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePayroll (@PathVariable Long id){
        boolean result = payrollService.deletePayroll(id);
        return result
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    //D(L) (Delete List)
    //여러 급여 이력 일괄 삭제
    @DeleteMapping
    public ResponseEntity<Boolean> deletePayrolls (@RequestBody List < Long > ids) {
        boolean result = payrollService.deletePayrolls(ids);
        return result
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
}
