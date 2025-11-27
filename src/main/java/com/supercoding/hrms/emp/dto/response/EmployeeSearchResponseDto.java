package com.supercoding.hrms.emp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmployeeSearchResponseDto {
    private Long empId;
    private Long empNo;
    private String empNm;
    private String deptNm;
    private String gradeNm;
    private String empStatusCd;
    private String accountStatusCd;
    private String hireDate;

}
