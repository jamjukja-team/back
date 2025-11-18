package com.supercoding.hrms.attendance.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    // TODO: 연관관계 맺기
    private Long empId;

    @Column(nullable = false)
    private LocalDateTime startTime; // 출근시간

    @Column
    private LocalDateTime endTime = null; // 퇴근시간

    @Column(nullable = false)
    private Boolean isOverTime = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // TODO: 연관관계 맺기
    @Column(nullable = false)
    private Long createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // TODO: 연관관계 맺기
    @Column
    private Long updatedBy;

    /**
     * 출근 메서드
     * @param empId
     * @param startTime
     */
    @Builder
    public Attendance(Long empId, LocalDateTime startTime) {
        this.empId = empId;
        this.startTime = startTime;
        this.createdBy = 0L;
    }

    /**
     * 출근 처리 메서드
     * @param empId
     * @param startTime
     * @param createdBy
     */
    public Attendance(Long empId, LocalDateTime startTime, Long createdBy) {
        this.empId = empId;
        this.startTime = startTime;
        this.createdBy = createdBy;
    }

    /**
     * 퇴근 메서드
     * @param endTime 퇴근시간
     * @param isOverTime 초과근무 여부
     * @param updatedBy 수정인 (틔근자?)
     */
    public void getOff(LocalDateTime endTime, Boolean isOverTime,Long updatedBy) {
        this.endTime = endTime;
        this.isOverTime = isOverTime;
        this.updatedBy = updatedBy;
    }

    /**
     * 업데이트 메서드 - 출퇴근 정정시
     * @param startTime 출근 시간
     * @param endTime 퇴근 시간
     * @param isOverTime 초과근무 여부
     * @param updatedBy 정정인
     */
    public void update (LocalDateTime startTime, LocalDateTime endTime, Boolean isOverTime, Long updatedBy) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOverTime = isOverTime;
        this.updatedBy = updatedBy;
    }
}
