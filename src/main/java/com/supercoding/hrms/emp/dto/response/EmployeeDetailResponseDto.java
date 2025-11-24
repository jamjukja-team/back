package com.supercoding.hrms.emp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDetailResponseDto {
    private String photo;
    private Long empId;
    private Long empNo;
    //부서정보
    private String deptId;
    private String deptNm;
    //직급정보
    private String gradeId;
    private String gradeNm;
    private String empNm;
    private String hireDate;
    //근속기간 필요
    private String phone;
    private String email;
    //연차
    private String accountStatusCd;
    private String empStatusCd;

}
