package com.supercoding.hrms.emp.controller;

import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.emp.dto.request.EmployeeSaveRequestDto;
import com.supercoding.hrms.emp.dto.request.EmployeeSearchRequestDto;
import com.supercoding.hrms.emp.dto.response.EmployeeDetailResponseDto;
import com.supercoding.hrms.emp.dto.response.EmployeeMetaDataResponseDto;
import com.supercoding.hrms.emp.dto.response.EmployeeSaveResponseDto;
import com.supercoding.hrms.emp.dto.response.EmployeeSearchResponseDto;
import com.supercoding.hrms.emp.service.EmpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/admin")
public class EmpController {
    private final EmpService empService;

    @PostMapping("/employees")
    public ResponseEntity<EmployeeSaveResponseDto> saveEmployee(@RequestBody @Valid EmployeeSaveRequestDto req) {
        return ResponseEntity.ok(empService.saveEmployee(req));
    }

    @PostMapping("/employees/search")
    public ResponseEntity<Page<EmployeeSearchResponseDto>> searchEmployees(@RequestBody EmployeeSearchRequestDto request, @PageableDefault(size = 10, sort = "empId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(empService.searchEmployees(request, pageable));
    }

    @GetMapping("/employees/{empId}")
    public EmployeeDetailResponseDto getEmployeeByAdmin(@PathVariable Long empId) {
        return empService.getEmployeeByAdmin(empId);
    }

    @PatchMapping("/employees/{empId}/lock")
    public ResponseEntity<CustomException> getAccLock(@PathVariable Long empId) {
        empService.getAccLock(empId);
        return ResponseEntity.ok(new CustomException(CustomMessage.SUCCESS_ACCOUNT_DISABLED));
    }

    @PatchMapping("/employees/{empId}/unlock")
    public ResponseEntity<CustomException> getAccUnlock(@PathVariable Long empId) {
        empService.getAccUnlock(empId);
        return ResponseEntity.ok(new CustomException(CustomMessage.SUCCESS_ACCOUNT_ENABLE));

    }
}
