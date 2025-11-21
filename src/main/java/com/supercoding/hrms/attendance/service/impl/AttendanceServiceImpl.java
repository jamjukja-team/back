package com.supercoding.hrms.attendance.service.impl;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.repository.AttendanceRepository;
import com.supercoding.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public AttendanceResponseDto create(CreateAttendanceRequestDto request) {
        Attendance attendance = request.of();
        attendanceRepository.save(attendance);
        return new AttendanceResponseDto(attendance);
    }

    @Override
    public List<AttendanceResponseDto> findAll(ReadAttendanceRequestDto request) {
        List<Attendance> attendances = attendanceRepository.findByConditions(
                request.getEmpId(),
                request.getStartTime(),
                request.getEndTime()
        );

        return attendances.stream().map(AttendanceResponseDto::new).toList();
    }

    @Override
    public AttendanceResponseDto findById(Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                // 익셉션 처리
        );
        return new AttendanceResponseDto(attendance);
    }

    @Override
    @Transactional
    public void getOff(Long id, GetOffAttendanceRequestDto request) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                // 익셉션 처리
        );

        attendance.getOff(request.getEndTime(), request.getIsOvertime(), request.getUpdateBy());
    }

    @Override
    @Transactional
    public void updateAttendance(Long id, UpdateAttendanceRequestDto request) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                // 익셉션 처리
        );
        attendance.update(
                request.getStartTime(),
                request.getEndTime(),
                request.getIsOvertime(),
                request.getUpdatedBy()
        );
    }

    @Override
    @Transactional
    public void delete(Collection<Long> ids) {
        List<Long> existingIds = attendanceRepository.findExistingIds(ids);
        Set<Long> existingIdSet = new HashSet<>(existingIds);

        Map<Long, Boolean> collect = ids.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        existingIdSet::contains
                ));

        if (collect.containsValue(false)) {
            // 익셉션 처리
        }
        attendanceRepository.deleteByIds(ids);
    }
}
