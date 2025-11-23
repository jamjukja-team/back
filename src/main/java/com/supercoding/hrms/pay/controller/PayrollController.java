package com.supercoding.hrms.pay.controller;

import com.supercoding.hrms.pay.domain.PayrollItem;
import com.supercoding.hrms.pay.dto.PayrollType;
import com.supercoding.hrms.pay.service.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor //생성자 자동 주입
public class PayrollController {

    private final PayrollService payrollService;

    //C (Create)
    //급여 이력 생성
    // 한달에 한번 자동 생성
    @PostMapping
    public void createPayroll(@RequestBody PayrollType request) {
        payrollService.loadPayrollFromJson();
    }

    //R(L) (Read List)
    //전체 급여 목록 조회 (관리자용)
    @GetMapping("/list")
    public ResponseEntity<List<PayrollType>> getPayrolls() {
        return ResponseEntity.ok(payrollService.getPayrolls());
    }

    //R (Read)
    //특정 급여 이력 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PayrollType> getPayroll(@PathVariable Long id) {
        // 특정 id를 넣어서 서비스의 getPayroll 실행
        PayrollType response = payrollService.getPayroll(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/itmes")
    public ResponseEntity<List<PayrollItem>> getPayrollItems(){
        return ResponseEntity.ok(payrollService.getPayrollItem());
    }

    //U (Update)
    //급여 이력 수정 (예: 상태, 날짜 등)
    @PutMapping()
    public ResponseEntity<Boolean> updatePayroll(
            @RequestBody PayrollType request) {
        try {
            payrollService.updatePayroll(request);
            return ResponseEntity.ok(true);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }
    }

    //D (Delete)
    //단일 급여 이력 삭제
    @DeleteMapping("/{histId}")
    public ResponseEntity<Boolean> deletePayroll (@PathVariable Long histId){
        boolean result = payrollService.deletePayroll(histId);
        return result
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    //D(L) (Delete List)
    //여러 급여 이력 일괄 삭제
    @DeleteMapping
    public ResponseEntity<Boolean> deletePayrolls (@RequestParam List < Long > payIds) {
        boolean result = payrollService.deletePayrolls(payIds);
        return result
                ? ResponseEntity.ok(true)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
}
