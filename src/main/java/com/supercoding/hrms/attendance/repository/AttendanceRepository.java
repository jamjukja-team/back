package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface AttendanceRepository {
    Attendance save(Attendance attendance);

    List<Attendance> findByEmpId(Long empId);
    List<Attendance> findByBetweenStartTime(LocalDateTime from, LocalDateTime to);

    void deleteByIds(Collection<Long> ids);
}
