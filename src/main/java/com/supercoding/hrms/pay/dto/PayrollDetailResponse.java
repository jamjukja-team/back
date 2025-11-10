package com.supercoding.hrms.pay.dto;

//“급여 내역 조회(직원)” 또는 “급여 상세(관리자)” 화면용 DTO
//한 명의 급여명세서 전체 구조를 전달.
import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollDetailResponse {
    private Long payHistId;             // 급여이력 ID
    private Long empId;                 // 직원 ID
    private String empName;             // 직원 이름
    private String deptName;            // 부서명
    private String payMonth;            // 급여월
    private String status;       // 상태
    private String payDate;          // 급여 지급일
    private Integer totalAmount;        // 총 금액
    private Integer actualAmount;       // 실지급액
    private List<ItemResponse> items;   // 상세 항목 리스트
}