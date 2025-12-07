package com.supercoding.hrms.attendance.dto.response;

import com.supercoding.hrms.attendance.dto.WeeklyAttendanceDto;
import com.supercoding.hrms.attendance.util.AttendanceSummary;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyAttendanceResponseDto {
    private List<WeeklyAttendanceDto> weeks;
    private AttendanceSummary summary;
}
