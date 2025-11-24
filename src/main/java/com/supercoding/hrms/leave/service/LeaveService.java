package com.supercoding.hrms.leave.service;

import com.supercoding.hrms.com.service.CommonMailService;
import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.dto.LeaveType;
import com.supercoding.hrms.leave.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
    // Leave 정보 CRUD 구현

    private final LeaveRepository leaveRepository;


    // 1. Create
    public TblLeave create(LeaveType leaveType) {
        TblLeave leave = leaveType.getLeaveInfo();
        return leaveRepository.save(leave);
    }

    // 2. Read (단건)
    public TblLeave read(Long leaveId) {
        return leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
    }

    // 3. Read (목록)
    public List<LeaveType> readList() {
        List<TblLeave> lists = leaveRepository.findAll();
        for(TblLeave i : lists){

        }
    }

    // 5. Delete (단건)
    public boolean delete(Long leaveId) {
        try {
            leaveRepository.deleteById(leaveId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 6. Delete (다건)
    public boolean deleteList(List<Long> leaveIds) {
        try {
            leaveIds.forEach(leaveRepository::deleteById);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // 승인 반려 처리
    public void updateStatus(Long leaveId, String reason, String status) {
        TblLeave leave = read(leaveId);
        leave.setLeaveStatus(status);
        leave.setRejectReason(reason);
        leaveRepository.save(leave);
    }

}
