package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDto> create(@RequestBody CreateAttendanceRequestDto request) {
        AttendanceResponseDto response = attendanceService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> getAll(@RequestBody ReadAttendanceRequestDto request) {
        List<AttendanceResponseDto> responses = attendanceService.findAll(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> getOne(@PathVariable Long id) {
        AttendanceResponseDto response = attendanceService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> getOff(@PathVariable Long id,
                                          @RequestBody GetOffAttendanceRequestDto request) {
        attendanceService.getOff(id, request);
        return ResponseEntity.ok(true);
    }
}
