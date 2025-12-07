package com.supercoding.hrms.attendance.dto.response;

import com.supercoding.hrms.attendance.dto.AdminWeeklyAttendanceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMonthlyAttendanceResponseDto {
    private Long empId;
    private Long empNo;
    private String empNm;
    private List<AdminWeeklyAttendanceDto> weeks;
}
