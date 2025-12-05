package com.supercoding.hrms.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ResetPasswordDto {
    private String token;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String newPassword;

}
