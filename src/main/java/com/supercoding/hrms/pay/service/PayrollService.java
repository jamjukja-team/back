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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        // 프론트에서 준 정보(request) 가지고 Payroll에 맵핑해줌
        Payroll payroll = Payroll.builder()
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();
        // 맵핑한 payroll을 DB(MySQL)에 저장
        Payroll savedPayroll = payrollRepository.save(payroll);

        // payrollDetail 저장
        syncPayrollDetails(request.getItems(), savedPayroll.getEmpId());

        // 3️⃣ 다시 조회해서 응답 반환
        return getPayroll(savedPayroll.getPayHistId());
    }

    // 한사람의 detail을 저장하거나 업데이트 함
    public void syncPayrollDetails(List<PayrollDetail> items, Long empId){
        // empId 기반으로 detail 불러옴
        List<PayrollDetail> details = payrollDetailRepository.findByEmpId(empId);
        List<String> itemCds = details.stream().map(PayrollDetail::getItemCd).toList();;

        List<PayrollDetail> newDetails = items.stream().map(item ->{
            if(itemCds.contains(item.getItemCd())){
                //detail id가 포함되면 업데이트
                return PayrollDetail.builder()
                        .payrollDetailId(details.get(itemCds.indexOf(item.getItemCd())).getPayrollDetailId())
                        .empId(empId)
                        .itemCd(item.getItemCd())
                        .amount(item.getAmount())
                        .remark(item.getRemark())
                        .build();
            } else{
                //포함안되면 저장
                return PayrollDetail.builder()
                        .empId(empId)
                        .itemCd(item.getItemCd())
                        .amount(item.getAmount())
                        .remark(item.getRemark())
                        .build();
            }
        }).toList();

        payrollDetailRepository.saveAll(newDetails);
    }

    //R (단건 조회)
    //급여 상세 조회 (직원/관리자 공통)
    //특정 payHistId 기준으로 급여명세서 세부 항목 조회
    public PayrollType getPayroll(Long histId) {
        // 조회하고자 하는 Payroll의 id(histId)를 통해 조회하고자 하는 payroll 불러옴
        Payroll payroll = payrollRepository.findById(histId)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        // 내가 반환하고자 하는 타입(PayrollType)으로 payroll을 맵핑해줌
        return new PayrollType(
                histId,
                payroll.getEmpId(),
                "김직원",
                "개발팀",
                0,
                0,
                PayrollStatus.from(payroll.getStatus()).getDisplayName(),
                payroll.getPayDate(),
                getDetails(payroll.getEmpId())
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
        // 조회하고자하는 payroll의 사원id를 사용하여 items 불러오는거
//        log.info("empId : {}", empId);
        return payrollDetailRepository.findByEmpId(empId);
    }

    //U (Update)
    @Transactional
    public void updatePayroll(PayrollType request) {
        Payroll payroll = Payroll.builder()
                .payHistId(request.getPayHistId())
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();

        // 매핑한 payroll로 업데이트
        payrollRepository.save(payroll);

        // payrollDetail은 따로 업데이트 해야 함.
        syncPayrollDetails(request.getItems(), request.getEmpId());
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
