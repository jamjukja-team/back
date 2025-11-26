package com.supercoding.hrms.emp.controller;


import com.supercoding.hrms.emp.dto.request.EmployeeSaveRequestDto;
import com.supercoding.hrms.emp.dto.request.EmployeeSearchRequestDto;
import com.supercoding.hrms.emp.dto.request.EmployeeUpdateRequestDto;
import com.supercoding.hrms.emp.dto.response.*;
import com.supercoding.hrms.emp.service.EmpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/hr/admin")
public class EmpController {
    private final EmpService empService;

    @PostMapping(value = "/employees", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeSaveResponseDto> saveEmployee(@RequestPart("data") @Valid EmployeeSaveRequestDto req, @RequestPart(value = "file", required = true) MultipartFile file) {
        System.out.println("============");
        System.out.println(req);
        System.out.println(file);
        System.out.println("============");
        return ResponseEntity.ok(empService.saveEmployee(req, file));
    }

    @PostMapping("/employees/search")
    public ResponseEntity<Page<EmployeeSearchResponseDto>> searchEmployees(@RequestBody EmployeeSearchRequestDto request, @PageableDefault(size = 10, sort = "empId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(empService.searchEmployees(request, pageable));
    }

    @GetMapping("/employees/{empId}/metadata")
    public EmployeeMetaDataResponseDto getEmployeeMetadata(@PathVariable Long empId) {

        return empService.getEmployeeMetadata(empId);
    }

    @GetMapping("/employees/{empId}")
    public EmployeeDetailResponseDto getEmployeeByAdmin(@PathVariable Long empId) {
        return empService.getEmployeeByAdmin(empId);
    }

    @PatchMapping("/employees/{empId}/lock")
    public ResponseEntity<String> getAccLock(@PathVariable Long empId) {
        empService.getAccLock(empId);
        return ResponseEntity.ok("계정이 비활성화되었습니다.");
    }

    @PatchMapping("/employees/update")
    public ResponseEntity<EmployeeUpdateResponseDto> updateEmployee(@RequestPart("data") @Valid EmployeeUpdateRequestDto req, @RequestPart(value = "file", required = true) MultipartFile photo) {
        return ResponseEntity.ok(empService.updateEmployee(req, photo));

    }
}
