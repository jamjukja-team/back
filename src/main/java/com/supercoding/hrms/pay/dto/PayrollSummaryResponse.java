package com.supercoding.hrms.pay.dto;

//급여 명세서 조회(관리자) 목록용 DTO
//한 줄당 한 명의 직원 급여 상태를 보여줌.
import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollSummaryResponse {
    private Long payHistId;      // 급여이력 ID
    private Long empId;          // 직원 ID
    private String empName;      // 직원 이름
    private String department;   // 부서명
    private String payMonth;     // 급여 기준월
    private int totalWorkHours;  // 총 근무시간
    private int overtimeHours;   // 연장 근무시간
    private int deduction;       // 공제액
    private int actualAmount;    // 실지급액
    private PayrollStatus status; // 상태(계산중, 확정, 지급완료)
}
