package com.supercoding.hrms.emp.controller;

import com.supercoding.hrms.emp.dto.EmployeeSaveRequestDto;
import com.supercoding.hrms.emp.dto.EmployeeSaveResponseDto;
import com.supercoding.hrms.emp.service.EmpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/admin")
public class EmpController {
    private final EmpService empService;

    @PostMapping("/employees")
    public ResponseEntity<EmployeeSaveResponseDto> saveEmployee(@RequestBody @Valid EmployeeSaveRequestDto req) {


        return ResponseEntity.ok(empService.saveEmployee(req));
    }
}
