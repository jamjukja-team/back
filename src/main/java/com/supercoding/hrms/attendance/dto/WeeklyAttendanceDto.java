package com.supercoding.hrms.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WeeklyAttendanceDto {
    private int weekNumber;
    private List<AttendanceDailyDto> days;
}
