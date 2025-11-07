package com.supercoding.hrms.pay.dto;

//급여 명세서 조회(관리자) 목록용 DTO
//한 줄당 한 명의 직원 급여 상태를 보여줌.
import com.supercoding.hrms.pay.domain.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayrollSummaryResponse {
    private Long payHistId;         // 급여이력 ID
    private Long empId;             // 직원 ID
    private String empName;         // 직원 이름
    private String deptName;        // 부서명
    private Integer workHour;       // 총 근무시간
    private Integer overHour;       // 연장 근무시간
    private Integer deduction;      // 공제액
    private Integer actualAmount;   // 실지급액
    private PayrollStatus status;   // 급여 상태
}