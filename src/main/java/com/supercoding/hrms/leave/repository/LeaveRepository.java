package com.supercoding.hrms.leave.repository;

import com.supercoding.hrms.leave.domain.TblLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository<테이블명, PK 타입>
public interface LeaveRepository extends JpaRepository<TblLeave, Long> {
    List<TblLeave> findByLeaveRegDateBetween(String start, String end);
}
