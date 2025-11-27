package com.supercoding.hrms.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveListResponse {
    private Long empId;         // 사번
    private Long empNo;         // 직원번호
    private String empNm;       // 직원명
    private String deptNm;      // 부서명
    private int leaveDuration;  // 휴가 기간
    private String gradeNm;     // 직급명
    private String hireDate;    // 입사일
}
