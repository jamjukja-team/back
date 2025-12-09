package com.supercoding.hrms.attendance.service;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.attendance.dto.AdminAttendanceDailyDto;
import com.supercoding.hrms.attendance.dto.AdminWeeklyAttendanceDto;
import com.supercoding.hrms.attendance.dto.response.AdminMonthlyAttendanceResponseDto;
import com.supercoding.hrms.attendance.repository.AttendanceRepository;
import com.supercoding.hrms.com.entity.CommonDetail;
import com.supercoding.hrms.com.repository.CommonDetailRepository;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.repository.LeaveRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAttendanceService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final CommonDetailRepository commonDetailRepository;


    public List<AdminMonthlyAttendanceResponseDto> listAttendances(String yyyymm, String deptId, String gradeId, String name) {

        String normalized = yyyymm.replace(".", "");
        YearMonth ym = YearMonth.parse(normalized, DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        Map<String, String> leaveTypeNameMap = commonDetailRepository
                .findByGroup_ComGroupCd("LEAVE_TYPE")
                .stream()
                .collect(Collectors.toMap(CommonDetail::getComCd, CommonDetail::getComNm));

        Specification<Employee> spec = (root, query, cb) -> null;
        spec = spec.and(deptEq(deptId));
        spec = spec.and(gradeEq(gradeId));
        spec = spec.and(nameContains(name));

        List<Employee> employees = employeeRepository.findAll(spec);
        List<AdminMonthlyAttendanceResponseDto> results = new ArrayList<>();

        for (Employee e : employees) {

            Map<LocalDate, Attendance> attendanceMap =
                    attendanceRepository.findByConditions(
                                    e.getEmpId(),
                                    start.atStartOfDay(),
                                    end.atTime(23, 59))
                            .stream()
                            .collect(Collectors.toMap(
                                    a -> a.getStartTime().toLocalDate(),
                                    a -> a,
                                    (oldV, newV) -> newV
                            ));

            Map<LocalDate, TblLeave> leaveMap = new HashMap<>();

            leaveRepository.findActiveLeave(e.getEmpId(), "ACTIVE", start.toString(), end.toString())
                    .forEach(l -> {

                        LocalDate leaveStart = LocalDate.parse(l.getLeaveStartDate());
                        LocalDate leaveEnd   = LocalDate.parse(l.getLeaveEndDate());

                        LocalDate cur = leaveStart;
                        while (!cur.isAfter(leaveEnd)) {
                            leaveMap.put(cur, l);
                            cur = cur.plusDays(1);
                        }
                    });

            List<AdminWeeklyAttendanceDto> weekList = new ArrayList<>();
            LocalDate cursor = start;

            while (!cursor.isAfter(end)) {

                int week = cursor.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                Attendance at = attendanceMap.get(cursor);
                TblLeave leave = leaveMap.get(cursor);

                AdminAttendanceDailyDto daily;

                // =============== 미래 날짜 ===============
                if (cursor.isAfter(LocalDate.now())) {
                    daily = AdminAttendanceDailyDto.builder()
                            .date(cursor.toString())
                            .weekNumber(week)
                            .build();
                }

                // =============== 휴가 ===============
                else if (leave != null) {

                    String type = leave.getLeaveType();     // ex) "AHL", "BHL", "AL"
                    String leaveName = leaveTypeNameMap.get(type); // ex) "오전반차", "연차"

                    int minutes = resolveLeaveMinutes(type); // 480 or 240

                    daily = AdminAttendanceDailyDto.builder()
                            .date(cursor.toString())
                            .weekNumber(week)
                            .workingMinutes(minutes)
                            .status(null)          // 휴가는 status 없음
                            .statusName(null)      // 휴가는 statusName 없음
                            .remarkCd(type)        // 그대로 AL, SL, EL, BHL, AHL
                            .remark(leaveName)     // DB com_nm: 연차, 병가, 오전반차...
                            .build();
                }

                // =============== 결근 ===============
                else if (at == null) {
                    daily = AdminAttendanceDailyDto.builder()
                            .date(cursor.toString())
                            .weekNumber(week)
                            .status("ABSENT")
                            .statusName("결근")
                            .workingMinutes(0)
                            .build();
                }

                // =============== 근무 ===============
                else {
                    LocalDateTime startDt = at.getStartTime();
                    LocalDateTime endDt   = at.getEndTime();

                    String status;
                    String statusName;
                    String remarkCd = null;
                    String remark   = null;

                    int minutes = at.calculateWorkingMinutes();   // 날짜 포함해서 전체 근무시간 계산

                    // 출근만 찍힌 경우 → 근무중
                    if (startDt != null && endDt == null) {
                        status = "WORKING";
                        statusName = "근무중";
                    }
                    // 출퇴근 모두 있는 경우
                    else if (startDt != null) {

                        // 1) 지각 여부
                        if (startDt.toLocalTime().isAfter(LocalTime.of(9, 0))) {
                            status = "LATE";
                            statusName = "지각";
                        } else {
                            status = "NORMAL";
                            statusName = "정상";
                        }

                        // 2) 연장근무 여부 (지각이든 아니든 공통 적용)
                        //    기준 : 출근한 '그 날'의 18:00 이후까지 일했는지
                        LocalDateTime standardEnd = startDt.toLocalDate().atTime(18, 0);
                        if (endDt != null && endDt.isAfter(standardEnd)) {
                            remarkCd = "OVERTIME";
                            remark   = "연장근무";
                        }

                    } else {
                        // 안전빵 기본값
                        status = "NORMAL";
                        statusName = "정상";
                    }

                    daily = AdminAttendanceDailyDto.builder()
                            .date(cursor.toString())
                            .weekNumber(week)
                            .startTime(startDt != null ? startDt.toString() : null)
                            .endTime(endDt != null ? endDt.toString() : null)
                            .workingMinutes(minutes)
                            .status(status)
                            .statusName(statusName)
                            .remarkCd(remarkCd)
                            .remark(remark)
                            .build();
                }

                addToWeekList(weekList, week, daily);
                cursor = cursor.plusDays(1);
            }

            results.add(
                    AdminMonthlyAttendanceResponseDto.builder()
                            .empId(e.getEmpId())
                            .empNo(e.getEmpNo())
                            .empNm(e.getEmpNm())
                            .weeks(weekList)
                            .build()
            );
        }

        return results;
    }

    private Specification<Employee> deptEq(String deptId) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            final String ALL = "DEPT_ALL";

            // deptId가 null이거나 ALL이면 필터를 적용하지 않음
            if (deptId == null || ALL.equals(deptId)) {
                return null;
            }

            return cb.equal(root.get("department").get("deptId"), deptId);
        };
    }

    private Specification<Employee> gradeEq(String gradeId) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            // 지역 변수로 정의
            final String ALL = "GRADE_ALL";

            if (gradeId == null || ALL.equals(gradeId)) {
                return null;
            }

            return cb.equal(root.get("grade").get("gradeId"), gradeId);
        };
    }


    private Specification<Employee> nameContains(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null : cb.like(root.get("empNm"), "%" + name + "%");
    }

    // ============================================================================
    private double resolveLeaveDay(String type) {
        return switch (type) {
            case "AL", "SL", "EL" -> 1.0;
            case "BHL", "AHL" -> 0.5;
            default -> 0;
        };
    }

    private int resolveLeaveMinutes(String type) {
        return switch (type) {
            case "AL", "SL", "EL" -> 480;
            case "BHL", "AHL" -> 240;
            default -> 0;
        };
    }

    private void addToWeekList(List<AdminWeeklyAttendanceDto> weekList, int week, AdminAttendanceDailyDto daily) {
        AdminWeeklyAttendanceDto target =
                weekList.stream()
                        .filter(w -> w.getWeekNumber() == week)
                        .findFirst()
                        .orElseGet(() -> {
                            AdminWeeklyAttendanceDto w = new AdminWeeklyAttendanceDto(week, new ArrayList<>());
                            weekList.add(w);
                            return w;
                        });

        target.getDays().add(daily);
    }
}
