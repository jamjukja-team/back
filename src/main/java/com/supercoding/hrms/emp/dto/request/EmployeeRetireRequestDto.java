package com.supercoding.hrms.emp.dto.request;

import lombok.Getter;

@Getter
public class EmployeeRetireRequestDto {
    private String reason;
    private String retireDate;
    private String registerId;
}
