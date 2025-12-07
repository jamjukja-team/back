package com.supercoding.hrms.leave.repository;

import com.supercoding.hrms.leave.domain.TblLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// JpaRepository<테이블명, PK 타입>
public interface LeaveRepository extends JpaRepository<TblLeave, Long> {
    List<TblLeave> findByLeaveRegDateBetween(String start, String end);

    @Query("""
                SELECT l
                FROM TblLeave l
                WHERE l.empId = :empId
                  AND l.leaveStatus = :status
                  AND l.leaveStartDate <= :endDate
                  AND l.leaveEndDate >= :startDate
            """)
    List<TblLeave> findActiveLeave(@Param("empId") Long empId, @Param("status") String status, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
