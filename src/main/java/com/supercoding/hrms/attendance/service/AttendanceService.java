package com.supercoding.hrms.attendance.service;

import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;

import java.util.Collection;
import java.util.List;

public interface AttendanceService {
    AttendanceResponseDto create(CreateAttendanceRequestDto request);
    List<AttendanceResponseDto> findAll(ReadAttendanceRequestDto request);
    AttendanceResponseDto findById(Long id);
    void getOff(Long id, GetOffAttendanceRequestDto request);
    void updateAttendance(Long id, UpdateAttendanceRequestDto request);
    void delete(Collection<Long> ids);
}
