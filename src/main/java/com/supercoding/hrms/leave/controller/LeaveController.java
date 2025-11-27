package com.supercoding.hrms.leave.controller;

import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.domain.TblLeaveCommonCode;
import com.supercoding.hrms.leave.dto.SelectType;
import com.supercoding.hrms.leave.repository.LeaveCommonCodeRepository;
import com.supercoding.hrms.leave.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor //생성자 자동 주입
public class LeaveController {
    // 프론트와 통신하는 역할 (CRUD 구현 -> API로 받아서 서비스한테 넘기는거)

    private final LeaveService leaveService;
    private final LeaveCommonCodeRepository leaveCommonCodeRepository;

    // Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart("leave") TblLeave leave, @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(leaveService.create(leave, file));
    }

    // Read – 단건 R
    @GetMapping("/{leaveId}")
    public ResponseEntity<?> read(@PathVariable Long leaveId) {
        return ResponseEntity.ok(leaveService.read(leaveId));
    }

    // Read(List) – 목록 R(L)
    @GetMapping
    public ResponseEntity<?> readList(
            @RequestParam(defaultValue = "") String startDate, @RequestParam(defaultValue = "") String endDate, @RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") Long empId, @RequestParam(defaultValue = "") String roleType, @RequestParam(defaultValue = "") String deptId, @RequestParam(defaultValue = "") String gradeId, @RequestParam(defaultValue = "") String empNm) {
        return ResponseEntity.ok(leaveService.readList(startDate, endDate, status, empId, roleType, deptId, gradeId, empNm));
    }

    // Delete – D
    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long leaveId) {
        boolean result = leaveService.delete(leaveId);
        return ResponseEntity.ok(result);
    }

    // Delete(List) – D(L)
    @DeleteMapping
    public ResponseEntity<Boolean> deleteList(@RequestParam List<Long> leaveIds) {
        boolean result = leaveService.deleteList(leaveIds);
        return ResponseEntity.ok(result);
    }

    //    SelectType(메타데이터) API
//    프론트가 이렇게 요청
//    GET /api/leave/select?grpCd=leave_type → 휴가 종류 목록 반환
//    GET /api/leave/select?grpCd=leave_status → 승인/반려 상태 목록 반환
    @GetMapping("/select")
    public List<SelectType> getSelectList(@RequestParam String grpCd) {

        // grpCd = leave_type 또는 leave_status
        List<TblLeaveCommonCode> list = leaveCommonCodeRepository.findByGrpCd(grpCd);

        List<SelectType> result = new ArrayList<>();

        for (TblLeaveCommonCode code : list) {
            SelectType dto = new SelectType();
            dto.setCd(code.getCd());
            dto.setNm(code.getNm());
            result.add(dto);
        }

        return result;
    }

    @PutMapping("/{leaveId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long leaveId,
            @RequestBody String reason,
            @RequestParam String status // 프론트에서 파라미터로 APPLY 혹은 REJECT 넘겨줘야 함.
    ) {
        leaveService.updateStatus(leaveId, reason, status);
        return ResponseEntity.ok("상태 업데이트 완료");
    }

}
