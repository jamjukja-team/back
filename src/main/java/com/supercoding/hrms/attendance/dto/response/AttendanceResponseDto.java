package com.supercoding.hrms.attendance.dto.response;

import com.supercoding.hrms.attendance.domain.Attendance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AttendanceResponseDto {
    private Long attendanceId;
    private Long empId;
    private String startTime;

    public AttendanceResponseDto(Attendance attendance) {
        this.attendanceId = attendance.getAttendanceId();
        this.empId = attendance.getEmployee().getEmpId();
        this.startTime = String.valueOf(attendance.getStartTime());
    }
}
