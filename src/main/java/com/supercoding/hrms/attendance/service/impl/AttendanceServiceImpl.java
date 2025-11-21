package com.supercoding.hrms.attendance.service.impl;

import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    @Override
    public AttendanceResponseDto create(CreateAttendanceRequestDto request) {
        return null;
    }

    @Override
    public List<AttendanceResponseDto> findAll(ReadAttendanceRequestDto request) {
        return List.of();
    }

    @Override
    public AttendanceResponseDto findById(Long id) {
        return null;
    }

    @Override
    public void getOff(Long id, GetOffAttendanceRequestDto request) {

    }

    @Override
    public void updateAttendance(Long id, UpdateAttendanceRequestDto request) {

    }

    @Override
    public void delete(Collection<Long> ids) {

    }
}
