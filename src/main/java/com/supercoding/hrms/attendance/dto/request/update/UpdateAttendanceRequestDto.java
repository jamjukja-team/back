package com.supercoding.hrms.attendance.dto.request.update;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAttendanceRequestDto {
    private String date;
    private String startTime;
    private String endTime;
}
