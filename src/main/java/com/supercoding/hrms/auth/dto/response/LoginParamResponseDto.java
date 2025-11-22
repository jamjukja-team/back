package com.supercoding.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginParamResponseDto {
    private String accessToken;
    private String refreshToken;

}
