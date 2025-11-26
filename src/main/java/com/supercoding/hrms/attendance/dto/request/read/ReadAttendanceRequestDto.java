package com.supercoding.hrms.attendance.dto.request.read;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReadAttendanceRequestDto {
    private Long empId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
