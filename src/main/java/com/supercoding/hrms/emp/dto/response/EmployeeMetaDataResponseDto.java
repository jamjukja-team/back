package com.supercoding.hrms.emp.dto.response;

import com.supercoding.hrms.com.dto.meta.DepartmentDto;
import com.supercoding.hrms.com.dto.meta.GradeDto;
import com.supercoding.hrms.emp.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmployeeMetaDataResponseDto {
    private List<DepartmentDto> departments;
    private List<GradeDto> grades;
    private EmployeeDetailResponseDto employeeDetailResponseDto;
}
