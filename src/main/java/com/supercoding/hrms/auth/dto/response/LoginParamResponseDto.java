package com.supercoding.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginParamResponseDto {
    private String accessToken;
    private String refreshToken;
    private String roleCd;
    private Long empId;
    private Long empNo;
    private String empNm;

}
