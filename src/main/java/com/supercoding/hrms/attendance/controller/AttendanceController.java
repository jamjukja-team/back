package com.supercoding.hrms.attendance.controller;

import com.supercoding.hrms.attendance.dto.response.MonthlyAttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.read.ReadAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.response.GetOffAttendanceResponseDto;
import com.supercoding.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping                            //출근
    public ResponseEntity<AttendanceResponseDto> create(@RequestBody CreateAttendanceRequestDto request) {
        AttendanceResponseDto response = attendanceService.create(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{attendanceId}")                    //퇴근
    public ResponseEntity<GetOffAttendanceResponseDto> getOff(@PathVariable Long attendanceId,
                                                              @RequestBody GetOffAttendanceRequestDto request) {
        GetOffAttendanceResponseDto response = attendanceService.getOff(attendanceId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{empId}/monthly")                    //한 사람 전체 조회
    public ResponseEntity<MonthlyAttendanceResponseDto> getOne(@PathVariable Long empId, @RequestParam String yyyymm) {
        return ResponseEntity.ok(attendanceService.findById(empId, yyyymm));
    }
}
