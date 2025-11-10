package com.supercoding.hrms.pay.service;

import com.supercoding.hrms.pay.domain.Payroll;
import com.supercoding.hrms.pay.domain.PayrollDetail;
import com.supercoding.hrms.pay.domain.PayrollStatus;
import com.supercoding.hrms.pay.dto.ItemResponse;
import com.supercoding.hrms.pay.dto.PayrollCreateRequest;
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
    //C, R, R(L), U, D, D(L) 규칙에 따라
    // 지금 R 다건으로 만들었음

    // C (Create), 급여 이력 생성
    public boolean createPayroll(PayrollCreateRequest request) {
        // PayrollCreateRequest → Payroll 변환
        Payroll payroll = Payroll.builder()
                .empId(request.getEmpId())
                .payMonth(request.getPayMonth())
                .status(request.getStatus())
                .build();
        Payroll saved = payrollRepository.save(payroll);
        if(saved != null) {
            return true;
        }
        return false;
    }

    //R (단건 조회)
    //급여 상세 조회 (직원/관리자 공통)
    //특정 payHistId 기준으로 급여명세서 세부 항목 조회
    public PayrollDetailResponse getPayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        // 🔹 PayrollDetailRepository를 사용해서 급여 항목 조회
        List<PayrollDetail> details = payrollDetailRepository.findByPayHistId(id);

        // 🔹 PayrollDetail → ItemResponse 변환
        List<ItemResponse> items = details.stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());

        int totalAmount = items.stream().mapToInt(ItemResponse::getAmount).sum();
        int actualAmount = totalAmount - 229500;

        return new PayrollDetailResponse(
                payroll.getPayHistId(),
                payroll.getEmpId(),
                "김직원",
                "개발팀",
                payroll.getPayMonth(),
                PayrollStatus.from(payroll.getStatus()).getDisplayName(),
                payroll.getPayDate(),
                totalAmount,
                actualAmount,
                items
        );
    }

    //R(L) (다건 조회)
    //[관리자용] 전체 급여 목록 조회
    public List<PayrollSummaryResponse> getPayrolls() {
        return payrollRepository.findAll().stream()
                .map(p -> new PayrollSummaryResponse(
                        p.getPayHistId(),
                        p.getEmpId(),
                        "김직원",
                        "개발팀",
                        160,
                        0,
                        229500,
                        2744820,
                        PayrollStatus.from(p.getStatus()).getDisplayName()
                ))
                .collect(Collectors.toList());
    }

    //U (Update)
    public PayrollDetailResponse updatePayroll(Long id, PayrollCreateRequest request) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        payroll.setPayMonth(request.getPayMonth());
        payroll.setStatus(request.getStatus());

        payrollRepository.save(payroll);
        return getPayroll(id);
    }

    //D (단건 삭제)
    public void deletePayroll(Long id) {
        payrollRepository.deleteById(id);
    }

    //D(L) (다건 삭제)
    public void deletePayrolls(List<Long> ids) {
        payrollRepository.deleteAllById(ids);
    }

    //Private Helper
    private ItemResponse mapToItemResponse(PayrollDetail detail) {
        return new ItemResponse(
                detail.getItemCd(),
                detail.getItemNm(),
                detail.getAmount(),
                detail.getRemark()
        );
    }

}
