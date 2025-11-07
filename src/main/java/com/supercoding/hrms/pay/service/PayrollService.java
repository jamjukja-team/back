package com.supercoding.hrms.pay.service;

import com.supercoding.hrms.pay.domain.Payroll;
import com.supercoding.hrms.pay.domain.PayrollDetail;
import com.supercoding.hrms.pay.dto.ItemResponse;
import com.supercoding.hrms.pay.dto.PayrollDetailResponse;
import com.supercoding.hrms.pay.dto.PayrollSummaryResponse;
import com.supercoding.hrms.pay.repository.ItemNmRepository;
import com.supercoding.hrms.pay.repository.PayrollDetailRepository;
import com.supercoding.hrms.pay.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollDetailRepository payrollDetailRepository;
    private final ItemNmRepository itemNmRepository;

    //[관리자용] 전체 급여 목록 조회
    public List<PayrollSummaryResponse> getPayrollSummaries() {
        return payrollRepository.findAll().stream()
                .map(p -> new PayrollSummaryResponse(
                        p.getPayHistId(),
                        p.getEmpId(),
                        "김직원",       // 임시 (Employee 연동 시 수정)
                        "개발팀",        // 임시 (Department 연동 시 수정)
                        160,            // 총 근무시간 (임시)
                        0,              // 연장 근무시간 (임시)
                        229500,         // 공제액 (임시)
                        2744820,        // 실지급액 (임시)
                        p.getStatus()
                ))
                .collect(Collectors.toList());
    }

    /* 급여 상세 조회 (직원/관리자 공통)
    - 특정 payHistId 기준으로 급여명세서 세부 항목 조회
    */
    public PayrollDetailResponse getPayrollDetail(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        List<ItemResponse> items = payroll.getDetails().stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());

        int totalAmount = items.stream().mapToInt(ItemResponse::getAmount).sum();
        int actualAmount = totalAmount - 229500; // [임시] 공제액 예시

        return new PayrollDetailResponse(
                payroll.getPayHistId(),
                payroll.getEmpId(),
                "김직원",          // [임시] Employee 연동 시 수정
                "개발팀",
                payroll.getPayMonth(),
                payroll.getStatus(),
                payroll.getPayDate(),
                totalAmount,
                actualAmount,
                items
        );
    }

    private ItemResponse mapToItemResponse(PayrollDetail detail) {
        return new ItemResponse(
                detail.getItem().getCd(),      // itemCode
                detail.getItem().getNm(),      // itemName
                detail.getAmount(),
                detail.getRemark()
        );
    }

}
