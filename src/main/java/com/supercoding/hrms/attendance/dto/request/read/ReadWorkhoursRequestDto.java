package com.supercoding.hrms.attendance.dto.request.read;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadWorkhoursRequestDto {
    private Long empId;
    private Long attendanceId;
    private Boolean isOvertime;
}
