package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository {
    Attendance save(Attendance attendance);
    Optional<Attendance> findById(Long id);
    List<Attendance> findByConditions(
            Long empId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
    List<Long> findExistingIds(Collection<Long> ids);
    void deleteByIds(Collection<Long> ids);
}
