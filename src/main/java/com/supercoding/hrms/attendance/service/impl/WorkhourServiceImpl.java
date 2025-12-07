package com.supercoding.hrms.attendance.service.impl;

import com.supercoding.hrms.attendance.domain.Workhour;
import com.supercoding.hrms.attendance.dto.request.read.ReadWorkhoursRequestDto;
import com.supercoding.hrms.attendance.dto.response.WorkhourResponseDto;
import com.supercoding.hrms.attendance.repository.WorkhourRepository;
import com.supercoding.hrms.attendance.service.WorkhourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkhourServiceImpl implements WorkhourService {

    private final WorkhourRepository workhourRepository;

    @Override
    public List<WorkhourResponseDto> getWorkhours(ReadWorkhoursRequestDto request) {
//        List<Workhour> workhours = workhourRepository.findByConditions(
//                request.getEmpId(),
//                request.getAttendanceId(),
//                request.getIsOvertime()
//        );
//
//        return workhours.stream().map(
//                WorkhourResponseDto::new
//        ).toList();
        return null;
    }

    @Override
    public WorkhourResponseDto getWorkhourById(Long id) {
//        Workhour workhour = workhourRepository.findById(id).orElseThrow(
//                // 익셉션 처리
//        );

        return null;//new WorkhourResponseDto(workhour);
    }
}
