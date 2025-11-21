package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long>, AttendanceRepository {

    @Query("SELECT a FROM Attendance a WHERE " +
            "(:empId IS NULL OR a.empId = :empId) AND " +
            "(:startTime IS NULL OR a.startTime >= :startTime) AND " +
            "(:endTime IS NULL OR a.endTime <= :endTime)")
    List<Attendance> findByConditions(
            @Param("empId") Long empId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT a.empId FROM Attendance a WHERE a.empId IN :ids")
    List<Long> findExistingIds(@Param("ids") Collection<Long> ids);

    @Query("DELETE FROM Attendance a WHERE a.attendanceId IN :ids")
    @Modifying
    void deleteByIds(@Param("ids") Collection<Long> ids);
}
