package com.supercoding.hrms.emp.dto.response;

import com.supercoding.hrms.emp.entity.Employee;
import lombok.Getter;

@Getter
public class EmployeeSaveResponseDto {
    private Long empId;
    private Long empNo;
    private String email;
    private String empNm;
    private String deptId;
    private String gradeId;
    private String roleCd;
    private String accountStatusCd;

    public EmployeeSaveResponseDto(Employee employee) {
        this.empId = employee.getEmpId();
        this.empNo = employee.getEmpNo();
        this.email = employee.getEmail();
        this.empNm = employee.getEmpNm();
        this.deptId = employee.getDepartment().getDeptId();
        this.gradeId = employee.getGrade().getGradeId();
        this.roleCd = employee.getRoleCd();
        this.accountStatusCd = employee.getAccountStatusCd();
    }
}
