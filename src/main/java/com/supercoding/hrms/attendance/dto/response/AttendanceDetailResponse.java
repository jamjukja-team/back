package com.supercoding.hrms.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AttendanceDetailResponse {
    private Long attendanceId;
    private Long empNo;
    private String empName;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String totalWorkingTime;
}
