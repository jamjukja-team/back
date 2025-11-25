package com.supercoding.hrms.emp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmployeeUpdateRequestDto {
    private Long empId;

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String empNm;

    @NotBlank(message = "생년월일을 입력해주세요.")
    private String birthDate;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "부서를 입력해주세요.")
    private String deptId;

    @NotBlank(message = "직급을 입력해주세요.")
    private String gradeId;

    @NotBlank(message = "올바른 접근이 아닙니다.")
    private String registerId;
}
