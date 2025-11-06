package com.supercoding.hrms.pay.dto;

//“급여 내역 조회(직원)” 또는 “급여 상세(관리자)” 화면용 DTO
//한 명의 급여명세서 전체 구조를 전달.
import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollDetailResponse {

    private Long payHistId;             // 급여이력 ID
    private Long empId;                 // 직원 ID
    private String empName;             // 직원 이름
    private String department;          // 부서명
    private String payMonth;            // 급여 기준월
    private PayrollStatus status;       // 급여 상태
    private LocalDate payDate;          // 지급일
    private int totalAmount;            // 총 지급액
    private int actualAmount;           // 실지급액
    private List<ItemResponse> items;   // 세부 항목 리스트
}
