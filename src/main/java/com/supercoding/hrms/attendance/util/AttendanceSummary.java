package com.supercoding.hrms.attendance.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummary {
    private int workDays;               // 실제 근무한 일수
    private int absentDays;             // 결근 일수
    private double leaveDays;           // 휴가 인정 일수 (연차 1.0 / 반차 0.5)
    private int totalWorkingMinutes;    // 총 근무 시간
    private double averageWorkingMinutes;
    private double attendanceRate;       // 출근율 (%)

    public void addWorkDay() { this.workDays++; }
    public void addAbsent() { this.absentDays++; }
    public void addLeaveDay(double day) { this.leaveDays += day; }
    public void addWorkingMinutes(int minutes) { this.totalWorkingMinutes += minutes; }

    public void finalizeSummary() {
        double dutyDays = workDays + absentDays + leaveDays;

        // 평균 근무 시간 = 총 근무 시간 ÷ 근무 대상 일수
        this.averageWorkingMinutes =
                dutyDays > 0 ? (double) totalWorkingMinutes / dutyDays : 0;

        // 출근율 = (실 출근 + 휴가 인정) ÷ 근무 대상 일수
        this.attendanceRate =
                dutyDays > 0 ? ((double)(workDays + leaveDays) / dutyDays) * 100 : 0;
    }
}
