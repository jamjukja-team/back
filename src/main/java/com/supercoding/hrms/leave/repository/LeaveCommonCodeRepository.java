package com.supercoding.hrms.leave.repository;

import com.supercoding.hrms.leave.domain.TblLeaveCommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveCommonCodeRepository extends JpaRepository<TblLeaveCommonCode, String> {
    List<TblLeaveCommonCode> findByGrpCd(String grpCd);
}
