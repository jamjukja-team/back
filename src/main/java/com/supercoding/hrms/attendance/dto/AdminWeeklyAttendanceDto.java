package com.supercoding.hrms.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminWeeklyAttendanceDto {
    private int weekNumber;
    private List<AdminAttendanceDailyDto> days;
}
