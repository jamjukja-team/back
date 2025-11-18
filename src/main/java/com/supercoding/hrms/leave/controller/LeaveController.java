package com.supercoding.hrms.leave.controller;

import com.supercoding.hrms.leave.domain.TblLeaveCommonCode;
import com.supercoding.hrms.leave.dto.LeaveType;
import com.supercoding.hrms.leave.dto.SelectType;
import com.supercoding.hrms.leave.repository.LeaveCommonCodeRepository;
import com.supercoding.hrms.leave.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public ResponseEntity<?> create(@RequestBody LeaveType leaveType) {
        return ResponseEntity.ok(leaveService.create(leaveType));
    }

    // Read – 단건 R
    @GetMapping("/{leaveId}")
    public ResponseEntity<?> read(@PathVariable Long leaveId) {
        return ResponseEntity.ok(leaveService.read(leaveId));
    }

    // Read(List) – 목록 R(L)
    @GetMapping
    public ResponseEntity<?> readList() {
        return ResponseEntity.ok(leaveService.readList());
    }

    // Delete – D
    @DeleteMapping("/{leaveId}")
    public ResponseEntity<?> delete(@PathVariable Long leaveId) {
        leaveService.delete(leaveId);
        return ResponseEntity.ok().build();
    }

    // Delete(List) – D(L)
    @DeleteMapping
    public ResponseEntity<?> deleteList(@RequestBody List<Long> leaveIds) {
        leaveService.deleteList(leaveIds);
        return ResponseEntity.ok().build();
    }

//    SelectType(드롭다운) API
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
    // 여기 드롭다운에서 leave_status 관리를 어떻게 해야 하는건지..?

    // 승인
    @PostMapping("/{leaveId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long leaveId) {
        leaveService.approve(leaveId);
        return ResponseEntity.ok("승인 완료");
    }

    @PostMapping("/{leaveId}/reject")
    public ResponseEntity<?> reject(
            @PathVariable Long leaveId,
            @RequestBody LeaveType leaveType) {

        leaveService.reject(leaveId, leaveType.getLeaveInfo().getRejectReason());
        return ResponseEntity.ok("반려 완료");
    }
}
