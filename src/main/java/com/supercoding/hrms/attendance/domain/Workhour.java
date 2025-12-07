package com.supercoding.hrms.attendance.domain;

import com.supercoding.hrms.emp.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "workhour")
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Getter
public class Workhour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workhourId;
//
//    @OneToMany(mappedBy = "workhour")
//    private List<Employee> employees;
//
//    @Column(nullable = false)
//    private Long attendanceId;
//
//    @Column(nullable = false)
//    private Long hour = 0L;
//
//    @Column(nullable = false)
//    private Long minute = 0L;
//
//    @Column(nullable = false)
//    private Boolean isOvertime = false;
//
//    private Long overtimeHour;
//
//    private Long overtimeMinute;
//
////    public Workhour(Long empId, Long attendanceId) {
////        this.empId = empId;
////        this.attendanceId = attendanceId;
////    }
//
//    /**
//     * 근무 계산
//     * @param minute 총근무 시간
//     */
//    public void setMinute(Long minute) {
//        this.hour = minute / 60;
//        this.minute = minute % 60;
//    }
//
//    /**
//     * 초과근무 계산
//     * @param minute 총근무시간 (9시간 이상)
//     */
//    public void setOvertimeMinute(Long minute) {
//        this.isOvertime = true;
//        long overtimeMinute = minute - (60 * 9);
//        this.overtimeHour = overtimeMinute / 60;
//        this.overtimeMinute = overtimeMinute % 60;
//    }

}
