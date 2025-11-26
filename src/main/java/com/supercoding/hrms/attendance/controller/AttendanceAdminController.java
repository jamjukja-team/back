package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.dto.request.delete.DeleteAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/admin/attendances")
public class AttendanceAdminController {
    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> findAll(@RequestBody ReadAttendanceRequestDto request) {
        List <AttendanceResponseDto> responses = attendanceService.findAll(request);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateAttendance(
            @PathVariable Long id,
            @RequestBody UpdateAttendanceRequestDto request) {
        attendanceService.updateAttendance(id,  request);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAttendances(@RequestBody DeleteAttendanceRequestDto request) {
        attendanceService.delete(request.getIds());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAttendance(@PathVariable Long id) {
        attendanceService.delete(List.of(id));
        return ResponseEntity.ok(true);
    }
}
