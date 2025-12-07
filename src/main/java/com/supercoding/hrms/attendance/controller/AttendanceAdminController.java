package com.supercoding.hrms.attendance.controller;

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


//    @GetMapping
//    public ResponseEntity<List<AttendanceResponseDto>> findAll(@RequestBody ReadAttendanceRequestDto request) {
//        List <AttendanceResponseDto> responses = attendanceService.findAll(request);
//        return ResponseEntity.ok(responses);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Boolean> updateAttendance(
//            @PathVariable Long id,
//            @RequestBody UpdateAttendanceRequestDto request) {
//        attendanceService.updateAttendance(id,  request);
//        return ResponseEntity.ok(true);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Boolean> deleteAttendances(@RequestBody DeleteAttendanceRequestDto request) {
//        attendanceService.delete(request.getIds());
//        return ResponseEntity.ok(true);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> deleteAttendance(@PathVariable Long id) {
//        attendanceService.delete(List.of(id));
//        return ResponseEntity.ok(true);
//    }
}
