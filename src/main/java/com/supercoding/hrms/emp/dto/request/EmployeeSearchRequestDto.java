package com.supercoding.hrms.emp.dto.request;

import lombok.Getter;

@Getter
public class EmployeeSearchRequestDto {
    private String deptId;
    private String gradeId;
    private String empStatusCd;
    private String accountStatusCd;
    private String keyword;
}
