package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.dto.request.update.UpdateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceDetailResponse;
import com.supercoding.hrms.attendance.service.AdminAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/admin/attendances")
public class AttendanceAdminController {
    private final AdminAttendanceService adminAttendanceService;

    @GetMapping("/monthly")
    public ResponseEntity<Object> listAttendances(@RequestParam String yyyymm, @RequestParam(required = false) String deptId, @RequestParam(required = false) String gradeId, @RequestParam(required = false) String name) {
        return ResponseEntity.ok(adminAttendanceService.listAttendances(yyyymm, deptId, gradeId, name));
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceDetailResponse> getAttendances(@PathVariable Long attendanceId) {
        return ResponseEntity.ok(adminAttendanceService.getAttendances(attendanceId));
    }

    @PatchMapping("/{attendanceId}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long attendanceId, @RequestBody UpdateAttendanceRequestDto request) {
        return ResponseEntity.ok(adminAttendanceService.updateAttendance(attendanceId, request));
    }
}
