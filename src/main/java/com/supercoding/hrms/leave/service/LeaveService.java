package com.supercoding.hrms.leave.service;

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
    public List<TblLeave> readList() {
        return leaveRepository.findAll();
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

    // 승인 처리
    public void approve(Long leaveId) {
        TblLeave leave = read(leaveId);
        leave.setLeaveStatus("APPLY"); // 승인 코드
        leaveRepository.save(leave);
    }

    // 반려 처리
    public void reject(Long leaveId, String reason) {
        TblLeave leave = read(leaveId);
        leave.setLeaveStatus("REJECT");
        leave.setRejectReason(reason);
        leaveRepository.save(leave);
    }

}
