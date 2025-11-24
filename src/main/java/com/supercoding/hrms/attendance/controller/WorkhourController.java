package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.dto.request.read.ReadWorkhoursRequestDto;
import com.supercoding.hrms.attendance.dto.response.WorkhourResponseDto;
import com.supercoding.hrms.attendance.service.WorkhourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/workhours")
public class WorkhourController {

    private final WorkhourService workhourService;

    @GetMapping
    public ResponseEntity<List<WorkhourResponseDto>> getWorkhours(@ModelAttribute ReadWorkhoursRequestDto request) {

    }
}
