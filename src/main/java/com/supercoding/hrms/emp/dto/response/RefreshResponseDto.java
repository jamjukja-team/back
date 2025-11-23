package com.supercoding.hrms.emp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
