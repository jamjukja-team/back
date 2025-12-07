package com.supercoding.hrms.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetOffAttendanceResponseDto {
    private Long attendanceId;
    private String endTime;
}
