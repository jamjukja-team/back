package com.supercoding.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LoginParamResponseDto {
    private String accessToken;
    private String refreshToken;
    private String roleCd;
    private Long empId;
    private Long empNo;
    private String empNm;
    private Long attendanceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
