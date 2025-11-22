package com.supercoding.hrms.auth.dto.request;

import lombok.Getter;

@Getter
public class SetPasswordRequestDto {
    private String email;
    private String newPassword;
}
