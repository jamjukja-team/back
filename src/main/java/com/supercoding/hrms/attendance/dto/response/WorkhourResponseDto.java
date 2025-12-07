package com.supercoding.hrms.attendance.dto.response;

import com.supercoding.hrms.attendance.domain.Workhour;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkhourResponseDto {
    private Long id;
    private Long empId;
    private Long attendanceId;
    private Long hour;
    private Long minute;
    private Boolean isOvertime;
    private Long overtimeHour;
    private Long overtimeMinute;

}
