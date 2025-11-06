package com.supercoding.hrms.pay.service;

import com.supercoding.hrms.pay.domain.Item;
import com.supercoding.hrms.pay.domain.Payroll;
import com.supercoding.hrms.pay.domain.PayrollDetail;
import com.supercoding.hrms.pay.domain.PayrollStatus;
import com.supercoding.hrms.pay.dto.ItemResponse;
import com.supercoding.hrms.pay.dto.PayrollCreateRequest;
import com.supercoding.hrms.pay.dto.PayrollDetailResponse;
import com.supercoding.hrms.pay.dto.PayrollSummaryResponse;
import com.supercoding.hrms.pay.repository.PayrollRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    /**
     * ✅ 급여 목록 조회 (관리자용)
     * - 모든 직원의 급여 이력 조회
     * - 향후 Employee 테이블과 join 예정
     */
    public List<PayrollSummaryResponse> getPayrollSummaries() {
        List<Payroll> payrollList = payrollRepository.findAll();

        return payrollList.stream()
                .map(p -> new PayrollSummaryResponse(
                        p.getPayHistId(),
                        p.getEmpId(),
                        "김직원",             // [임시] Employee 테이블 연동 시 이름으로 대체
                        "개발팀",              // [임시] 부서명
                        p.getPayMonth(),
                        168, 8,               // [임시] 근무시간/연장시간
                        229500,               // [임시] 공제액
                        2744820,              // [임시] 실지급액
                        p.getStatus()
                ))
                .collect(Collectors.toList());
    }

    /**
     * ✅ 급여 상세 조회 (직원/관리자 공통)
     * - 특정 payHistId 기준으로 급여명세서 세부 항목 조회
     */
    public PayrollDetailResponse getPayrollDetail(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        // 급여 상세 항목 리스트 변환
        List<ItemResponse> items = payroll.getDetails().stream()
                .map(detail -> mapToItemResponse(detail))
                .collect(Collectors.toList());

        int totalAmount = items.stream().mapToInt(ItemResponse::getAmount).sum();
        int actualAmount = totalAmount - 229500; // [임시] 공제액 예시

        return new PayrollDetailResponse(
                payroll.getPayHistId(),
                payroll.getEmpId(),
                "김직원",               // [임시] Employee 연동 시 수정
                "개발팀",
                payroll.getPayMonth(),
                payroll.getStatus(),
                payroll.getPayDate(),
                totalAmount,
                actualAmount,
                items
        );
    }

    // ✅ 급여상세 항목 변환 메서드 (내부 전용)
    private ItemResponse mapToItemResponse(PayrollDetail detail) {
        return new ItemResponse(
                detail.getItem().getCd(),
                detail.getItem().getNm(),
                detail.getAmount(),
                detail.getRemark()
        );
    }

    // ✅ 급여 상태 변경 (관리자용)
    @Transactional
    public void updatePayrollStatus(Long id, PayrollStatus status) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        payroll.setStatus(status);
    }

    @Transactional
    public Payroll createPayroll(PayrollCreateRequest request) {
        // 1️⃣ Payroll 객체 생성
        Payroll payroll = new Payroll();
        payroll.setEmpId(request.getEmpId());
        payroll.setPayMonth(request.getPayMonth());
        payroll.setPayDate(request.getPayDate());
        payroll.setStatus(request.getStatus());

        // 2️⃣ PayrollDetail 리스트 생성
        List<PayrollDetail> details = request.getItems().stream().map(itemReq -> {
            PayrollDetail detail = new PayrollDetail();
            Item item = new Item();
            item.setCd(itemReq.getItemCode());  // 이미 DB에 있으면 findById로 교체 가능
            detail.setItem(item);
            detail.setAmount(itemReq.getAmount());
            detail.setRemark(itemReq.getRemark());
            detail.setPayroll(payroll);
            detail.setEmpId(request.getEmpId());
            return detail;
        }).collect(Collectors.toList());

        payroll.setDetails(details);

        // 3️⃣ DB 저장
        return payrollRepository.save(payroll);
    }
}
