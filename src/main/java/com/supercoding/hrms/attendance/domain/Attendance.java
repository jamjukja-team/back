package com.supercoding.hrms.attendance.domain;

import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.emp.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Getter
@Setter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(nullable = false)
    private LocalDateTime startTime; // 출근시간

    @Column
    private LocalDateTime endTime; // 퇴근시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    public int calculateWorkingMinutes() {
        if (startTime == null || endTime == null) return 0;
        return (int) Duration.between(startTime, endTime).toMinutes();
    }

}
