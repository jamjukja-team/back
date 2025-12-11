package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long>, AttendanceRepository {

    @Query("""
            SELECT a FROM Attendance a
            WHERE (:id IS NULL OR a.employee.empId = :id)
              AND (:startTime IS NULL OR a.startTime >= :startTime)
              AND (:endTime IS NULL OR a.endTime IS NULL OR a.endTime <= :endTime)
            """)
    List<Attendance> findByConditions(
            @Param("id") Long empId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT t FROM Attendance t " +
            "WHERE t.startTime >= :start " +
            "AND t.startTime < :end " +
            "AND t.employee.empId = :empId")
    List<Attendance> findByStartTimeBetweenAndEmployee_EmpId(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("empId") Long empId
    );
    @Query("""
    SELECT t 
      FROM Attendance t
     WHERE t.employee.empId = :empId
       AND t.startTime >= :start
       AND t.startTime < :end
     ORDER BY t.attendanceId DESC
    """)
    Attendance findTodayLatestAttendance(
            @Param("empId") Long empId,
            @Param("start") LocalDateTime startOfDay,
            @Param("end") LocalDateTime endOfDay
    );
//
//    @Query("SELECT a.empId FROM Attendance a WHERE a.empId IN :ids")
//    List<Long> findExistingIds(@Param("ids") Collection<Long> ids);
//
//    @Query("DELETE FROM Attendance a WHERE a.attendanceId IN :ids")
//    @Modifying
//    void deleteByIds(@Param("ids") Collection<Long> ids);
}
