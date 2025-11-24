package com.supercoding.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenResponseDto {
    String accessToken;
    String roleCd;
}
