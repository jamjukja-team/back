package com.supercoding.hrms.pay.dto;

import com.supercoding.hrms.pay.domain.PayrollDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollType {
    private Long payHistId;             // 급여이력 ID
    private Long empId;                 // 직원 ID
    private String empName;             // 직원 이름
    private String deptName;            // 부서명
    private Integer workHour;       // 총 근무시간
    private Integer overHour;       // 연장 근무시간
    private String status;       // 상태
    private String payDate; // 지급일
    private List<PayrollDetail> items;
}
