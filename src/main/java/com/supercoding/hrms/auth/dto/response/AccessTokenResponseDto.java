package com.supercoding.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenResponseDto {
    private String accessToken;
    private String roleCd;
    private Long empId;
    private Long empNo;
    private String empNm;
}
