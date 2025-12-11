package com.supercoding.hrms.attendance.service;

import com.supercoding.hrms.attendance.dto.response.MonthlyAttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.response.GetOffAttendanceResponseDto;

public interface AttendanceService {
    AttendanceResponseDto create(CreateAttendanceRequestDto request);
    MonthlyAttendanceResponseDto findById(Long id, String yyyymm);
    GetOffAttendanceResponseDto getOff(Long id, GetOffAttendanceRequestDto request);
}
