package com.supercoding.hrms.emp.dto.request;

import lombok.Getter;

@Getter
public class EmployeeSearchRequestDto {
    private String deptFilterCd;
    private String gradeFilterCd;
    private String empStatusCd;
    private String accountStatusCd;
    private String keyword;
}
