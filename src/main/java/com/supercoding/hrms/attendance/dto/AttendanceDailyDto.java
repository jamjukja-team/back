package com.supercoding.hrms.attendance.dto;

import com.supercoding.hrms.attendance.domain.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AttendanceDailyDto {
    private String date;     // yyyy-MM-dd
    private String startTime;
    private String endTime;
    private int workingMinutes;
    private int weekNumber;
    private String status;
    private String statusName;
    private String remarkCd;
    private String remark;

    public static AttendanceDailyDto future(String date, int week) {
        return new AttendanceDailyDto(date,null, null, 0, week, null, null, null, null);
    }

    public static AttendanceDailyDto absent(String date, int week) {
        return new AttendanceDailyDto(date, null, null, 0, week, "ABSENT", "결근", null, null);
    }

}
