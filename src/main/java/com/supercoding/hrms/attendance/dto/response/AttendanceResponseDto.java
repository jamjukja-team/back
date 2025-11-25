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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isOverTime;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    public AttendanceResponseDto(Attendance attendance) {
        this.attendanceId = attendance.getAttendanceId();
        this.empId = attendance.getEmpId();
        this.startTime = attendance.getStartTime();
        this.endTime = attendance.getEndTime();
        this.createdAt = attendance.getCreatedAt();
        this.createdBy = attendance.getCreatedBy();
        this.updatedAt = attendance.getUpdatedAt();
        this.updatedBy = attendance.getUpdatedBy();
    }
}
