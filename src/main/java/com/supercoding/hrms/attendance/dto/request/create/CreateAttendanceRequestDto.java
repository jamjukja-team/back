package com.supercoding.hrms.attendance.dto.request.create;

import com.supercoding.hrms.attendance.domain.Attendance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateAttendanceRequestDto {
    private Long empId;
    private String startTime;
}
