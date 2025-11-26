package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.domain.Workhour;
import com.supercoding.hrms.attendance.dto.request.read.ReadWorkhoursRequestDto;
import com.supercoding.hrms.attendance.dto.response.WorkhourResponseDto;
import com.supercoding.hrms.attendance.service.WorkhourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/workhours")
public class WorkhourController {

    private final WorkhourService workhourService;

    @GetMapping
    public ResponseEntity<List<WorkhourResponseDto>> getWorkhours(@ModelAttribute ReadWorkhoursRequestDto request) {
        List<WorkhourResponseDto> responses = workhourService.getWorkhours(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkhourResponseDto> getWorkhour(@PathVariable Long id) {
        WorkhourResponseDto response = workhourService.getWorkhourById(id);
        return ResponseEntity.ok(response);
    }
}
