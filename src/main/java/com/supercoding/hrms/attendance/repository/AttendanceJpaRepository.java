package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long>, AttendanceRepository {

    @Query("DELETE FROM Attendance a WHERE a.attendanceId IN :ids")
    @Modifying
    void deleteByIds(@Param("ids") Collection<Long> ids);
}
