package com.supercoding.hrms.emp.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeUpdateResponseDto {
    private Long empId;
    private Long empNo;
    private String email;
    private String empNm;
    private String birthDate;
    private String hireDate;
    private String phone;
    private String deptId;
    private String deptNm;
    private String gradeId;
    private String gradeNm;
    private String photo;
    private String updateAt;
    private String upEmpId;
}
