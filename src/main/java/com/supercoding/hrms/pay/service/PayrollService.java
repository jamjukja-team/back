package com.supercoding.hrms.pay.service;

import com.supercoding.hrms.pay.domain.Payroll;
import com.supercoding.hrms.pay.domain.PayrollDetail;
import com.supercoding.hrms.pay.domain.PayrollStatus;
import com.supercoding.hrms.pay.dto.PayrollType;
import com.supercoding.hrms.pay.repository.ItemNmRepository;
import com.supercoding.hrms.pay.repository.PayrollDetailRepository;
import com.supercoding.hrms.pay.repository.PayrollRepository;
import jakarta.transaction.Transactional;
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
    public PayrollType createPayroll(PayrollType request) {
        // PayrollCreateRequest → Payroll 변환
        Payroll payroll = Payroll.builder()
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();
        Payroll savedPayroll = payrollRepository.save(payroll);

        // 2️⃣ items → PayrollDetail로 변환 후 저장
        syncPayrollDetails(request.getItems(), savedPayroll.getPayHistId(), savedPayroll.getEmpId());

        // 3️⃣ 다시 조회해서 응답 반환
        return getPayroll(savedPayroll.getPayHistId());
    }

    public void syncPayrollDetails(List<PayrollDetail> items, Long histId, Long empId){

        List<PayrollDetail> details = items.stream()
                .map(item -> {
                    PayrollDetail.PayrollDetailBuilder builder = PayrollDetail.builder()
                            .empId(empId)
                            .itemCd(item.getItemCd())
                            .amount(item.getAmount())
                            .remark(item.getRemark());

                    // ✅ update 모드일 경우 (detail pk 존재)
                    if (histId != null) {
                        builder.payrollDetailId(histId); // detail의 PK를 직접 지정
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());

        payrollDetailRepository.saveAll(details);
    }


    //R (단건 조회)
    //급여 상세 조회 (직원/관리자 공통)
    //특정 payHistId 기준으로 급여명세서 세부 항목 조회
    public PayrollType getPayroll(Long empId) {
        Payroll payroll = payrollRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        // 🔹 PayrollDetailRepository를 사용해서 급여 항목 조회

        return new PayrollType(
                payroll.getPayHistId(),
                payroll.getEmpId(),
                "김직원",
                "개발팀",
                0,
                0,
                PayrollStatus.from(payroll.getStatus()).getDisplayName(),
                payroll.getPayDate(),
                getDetails(empId)
        );
    }

    //R(L) (다건 조회)
    //[관리자용] 전체 급여 목록 조회
    public List<PayrollType> getPayrolls() {
        return payrollRepository.findAll().stream()
                .map(p -> new PayrollType(
                        p.getPayHistId(),
                        p.getEmpId(),
                        "김직원",
                        "개발팀",
                        160,
                        0,
                        PayrollStatus.from(p.getStatus()).getDisplayName(),
                        "250926",
                        getDetails(p.getEmpId())
                ))
                .collect(Collectors.toList());
    }

    public List<PayrollDetail> getDetails(Long empId){
        return payrollDetailRepository.findByEmpId(empId);
    }

    //U (Update)
    @Transactional
    public void updatePayroll(Long id, PayrollType request) {
        Payroll payroll = Payroll.builder()
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();

        Payroll savePayroll = payrollRepository.save(payroll);

        payrollRepository.save(savePayroll);

        syncPayrollDetails(request.getItems(), request.getPayHistId(), request.getEmpId());

    }

    //D (단건 삭제)
    public boolean deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            return false; // 해당 ID 없음
        }

        try {
            payrollRepository.deleteById(id);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 삭제 중 오류
        }
    }

    //D(L) (다건 삭제)
    public boolean deletePayrolls(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false; // 삭제할 ID가 없음
        }

        try {
            payrollRepository.deleteAllById(ids);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 중간에 오류 발생
        }
    }


}
