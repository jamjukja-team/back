package com.supercoding.hrms.attendance.repository;

import com.supercoding.hrms.attendance.domain.Workhour;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkhourRepository {
    Workhour save(Workhour workhour);

    Optional<Workhour> findById(Long id);
    Workhour findByAttendanceId(Long id);
    List<Workhour> findAllByAttendanceIds(Collection<Long> ids);
    List<Workhour> findByConditions(Long empId, Long attendanceId, Boolean isOverTime);
    void deleteByIds(Collection<Long> ids);
}
