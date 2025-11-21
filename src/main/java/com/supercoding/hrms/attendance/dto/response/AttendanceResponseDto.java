package com.supercoding.hrms.attendance.dto.response;

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
}
