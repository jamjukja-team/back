package com.supercoding.hrms.attendance.dto.request.create;

import com.supercoding.hrms.attendance.domain.Attendance;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateAttendanceRequestDto {
    private Long empId;
    private LocalDateTime startTime;
    private Long createdBy;

    public CreateAttendanceRequestDto(Long empId, LocalDateTime startTime, Long createdBy) {
        this.empId = empId;
        this.startTime = startTime;
        if (createdBy == null) {
            this.createdBy = 0L;
        }
        this.createdBy = createdBy;
    }

    public Attendance of() {
        return new Attendance(
                empId, startTime, createdBy
        );
    }
}
