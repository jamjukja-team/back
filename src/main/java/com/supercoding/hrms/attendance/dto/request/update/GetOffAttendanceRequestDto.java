package com.supercoding.hrms.attendance.dto.request.update;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetOffAttendanceRequestDto {
    private LocalDateTime endTime;
    private Boolean isOvertime;
    private Long updateBy;
}
