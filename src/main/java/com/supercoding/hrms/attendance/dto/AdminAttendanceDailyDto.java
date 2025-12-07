package com.supercoding.hrms.attendance.dto;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.com.entity.CommonDetail;
import com.supercoding.hrms.leave.domain.TblLeave;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class AdminAttendanceDailyDto {

    private String date;
    private String startTime;
    private String endTime;
    private int workingMinutes;
    private int weekNumber;
    private String status;
    private String statusName;
    private String remarkCd;
    private String remark;

}
