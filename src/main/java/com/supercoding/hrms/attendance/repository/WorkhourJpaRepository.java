package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Workhour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WorkhourJpaRepository extends JpaRepository<Workhour,Long>, WorkhourRepository {
//    @Query("DELETE FROM Workhour w WHERE w.workhourId IN :ids")
//    @Modifying
//    void deleteByIds(@Param("ids") Collection<Long> ids);
//
//    @Query("SELECT w FROM Workhour w WHERE w.attendanceId IN :ids")
//    List<Workhour> findAllByAttendanceIds(@Param("ids") Collection<Long> ids);
//
//    @Query("SELECT w FROM Workhour w WHERE " +
//            "(:empId IS NULL OR w.empId = :empId) AND " +
//            "(:attendanceId IS NULL OR w.attendanceId = :attendanceId) AND " +
//            "(:isOverTime IS NULL OR w.isOvertime = :isOverTime)")
//    List<Workhour> findByConditions(Long empId, Long attendanceId, Boolean isOverTime);
}
