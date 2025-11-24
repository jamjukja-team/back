package com.supercoding.hrms.attendance.service;

import com.supercoding.hrms.attendance.dto.request.read.ReadWorkhoursRequestDto;
import com.supercoding.hrms.attendance.dto.response.WorkhourResponseDto;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface WorkhourService {
    List<WorkhourResponseDto> getWorkhours(ReadWorkhoursRequestDto request);
    WorkhourResponseDto getWorkhourById(Long id);
}
