package com.supercoding.hrms.attendance.service.impl;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.attendance.domain.Workhour;
import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.repository.AttendanceRepository;
import com.supercoding.hrms.attendance.service.AttendanceService;
import com.supercoding.hrms.attendance.repository.WorkhourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final WorkhourRepository workhourRepository;


    @Override
    @Transactional
    public AttendanceResponseDto create(CreateAttendanceRequestDto request) {
        Attendance attendance = request.of();
        Attendance saved = attendanceRepository.save(attendance);
        Workhour workhour = new Workhour(request.getEmpId(), saved.getAttendanceId());
        workhourRepository.save(workhour);
        return new AttendanceResponseDto(saved);
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

        Workhour workhour = workhourRepository.findByAttendanceId(attendance.getAttendanceId());

        if (workhour == null) {
            // 익셉션 처리
        }

        attendance.getOff(request.getEndTime(), request.getUpdateBy());

        Long between = Duration.between(attendance.getStartTime(), attendance.getEndTime()).toMinutes();

        if (between > 60 * 9) {
            workhour.setOvertimeMinute(between);
        } else {
            workhour.setMinute(between);
        }
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
                request.getUpdatedBy()
        );


        Workhour workhour = workhourRepository.findByAttendanceId(attendance.getAttendanceId());

        Long between = Duration.between(attendance.getStartTime(), attendance.getEndTime()).toMinutes();

        if (between > 60 * 9) {
            workhour.setOvertimeMinute(between);
        } else {
            workhour.setMinute(between);
        }

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
        List<Workhour> workhours = workhourRepository.findAllByAttendanceIds(existingIds);
        Set<Long> workhourIds = workhours.stream().map(Workhour::getAttendanceId).collect(Collectors.toSet());
        Map<Long, Boolean> workhourCollects = workhourIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        workhourIds::contains
                ));
        if (collect.containsValue(false) || workhourCollects.containsValue(false) || workhourCollects.size() != collect.size()) {
            // 익셉션 처리
        }
        workhourRepository.deleteByIds(workhourIds);
        attendanceRepository.deleteByIds(ids);
    }
}
