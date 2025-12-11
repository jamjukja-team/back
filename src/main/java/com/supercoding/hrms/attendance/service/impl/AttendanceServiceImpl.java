package com.supercoding.hrms.attendance.service.impl;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.attendance.dto.AttendanceDailyDto;
import com.supercoding.hrms.attendance.dto.response.MonthlyAttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.WeeklyAttendanceDto;
import com.supercoding.hrms.attendance.dto.request.create.CreateAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.request.update.GetOffAttendanceRequestDto;
import com.supercoding.hrms.attendance.dto.response.AttendanceResponseDto;
import com.supercoding.hrms.attendance.dto.response.GetOffAttendanceResponseDto;
import com.supercoding.hrms.attendance.repository.AttendanceRepository;
import com.supercoding.hrms.attendance.service.AttendanceService;
import com.supercoding.hrms.attendance.util.AttendanceSummary;
import com.supercoding.hrms.com.entity.CommonDetail;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.com.repository.CommonDetailRepository;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final CommonDetailRepository commonDetailRepository;

    @Override
    @Transactional
    public AttendanceResponseDto create(CreateAttendanceRequestDto request) {

        Employee employee = employeeRepository.findById(request.getEmpId())
                .orElseThrow(() -> new CustomException(CustomMessage.EMPLOYEE_NOT_FOUND));

        Attendance attendance = attendanceRepository.save(
                Attendance.builder()
                        .startTime(LocalDateTime.parse(request.getStartTime()))
                        .employee(employee)
                        .build()
        );

        return new AttendanceResponseDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyAttendanceResponseDto findById(Long empId, String yyyymm) {

        String replaceYm = yyyymm.replace(".", "");
        YearMonth ym = YearMonth.parse(replaceYm, DateTimeFormatter.ofPattern("yyyyMM"));

        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);

        Map<LocalDateTime, Attendance> attendanceMap = loadAttendanceMap(empId, start, end);
        Map<LocalDateTime, TblLeave> leaveMap = loadLeaveMap(empId, start, end);

        return buildMonthlyResult(ym, attendanceMap, leaveMap);
    }


    private void addToWeekList(List<WeeklyAttendanceDto> weeks, int week, AttendanceDailyDto dto) {
        WeeklyAttendanceDto target = weeks.stream()
                .filter(w -> w.getWeekNumber() == week)
                .findFirst()
                .orElse(null);

        if (target == null) {
            target = new WeeklyAttendanceDto(week, new ArrayList<>());
            weeks.add(target);
        }

        target.getDays().add(dto);
    }


    private MonthlyAttendanceResponseDto buildMonthlyResult(
            YearMonth ym,
            Map<LocalDateTime, Attendance> attendanceMap,
            Map<LocalDateTime, TblLeave> leaveMap
    ) {
        Map<String, String> leaveTypeNameMap = commonDetailRepository
                .findByGroup_ComGroupCd("LEAVE_TYPE")
                .stream()
                .collect(Collectors.toMap(CommonDetail::getComCd, CommonDetail::getComNm));

        List<WeeklyAttendanceDto> weekList = new ArrayList<>();
        AttendanceSummary summary = new AttendanceSummary();

        LocalDateTime cursor = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);

        while (!cursor.toLocalDate().isAfter(end.toLocalDate())) {

            int week = cursor.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
            LocalDate keyDate = cursor.toLocalDate();
            LocalDateTime key = keyDate.atStartOfDay();

            Attendance att = attendanceMap.get(key);
            TblLeave leave = leaveMap.get(key);

            AttendanceDailyDto daily;

            // ---------- 휴가 ----------
            if (leave != null) {

                String type = leave.getLeaveType();    // AL / SL / EL / BHL / AHL
                String leaveName = leaveTypeNameMap.get(type); // 연차 / 병가 / 경조 / 오전반차 / 오후반차
                int minutes = resolveLeaveMinutes(type);
                double leaveDay = resolveLeaveDay(type);

                summary.addLeaveDay(leaveDay);
                summary.addWorkingMinutes(minutes);

                daily = AttendanceDailyDto.builder()
                        .date(keyDate.toString())
                        .weekNumber(week)
                        .status(null)
                        .statusName(null)
                        .workingMinutes(minutes)
                        .remarkCd(type)          // 휴가 코드를 그대로 remarkCd 로 사용
                        .remark(leaveName)       // 휴가명을 remark로 사용
                        .build();

                addToWeekList(weekList, week, daily);
                cursor = cursor.plusDays(1);
                continue;
            }

            // ---------- 결근 ----------
            if (att == null) {
                LocalDate today = LocalDate.now();

                if (keyDate.isAfter(today)) {
                    daily = AttendanceDailyDto.future(keyDate.toString(), week);
                } else {
                    summary.addAbsent();
                    daily = AttendanceDailyDto.absent(keyDate.toString(), week);
                }

                addToWeekList(weekList, week, daily);
                cursor = cursor.plusDays(1);
                continue;
            }

            // ---------- 정상 근무/지각/연장 ----------
            LocalDateTime startDt = att.getStartTime();
            LocalDateTime endDt = att.getEndTime();

            int minutes = att.calculateWorkingMinutes();
            summary.addWorkDay();
            summary.addWorkingMinutes(minutes);

            String status;
            String statusName;
            String remarkCd = null;
            String remark = null;

            boolean late = startDt.toLocalTime().isAfter(LocalTime.of(9, 0));
            boolean overtime = (endDt != null && minutes > 8 * 60);

            if (endDt == null) {
                status = "WORKING";
                statusName = "근무중";
            }
            else if (late) {
                status = "LATE";
                statusName = "지각";
            }
            else {
                status = "NORMAL";
                statusName = "정상";
            }

            //지각이어도 ‘연장근무’는 remarkCd/remark 로 기록됨
            if (overtime) {
                remarkCd = "OVERTIME";
                remark = "연장근무";
            }

            daily = AttendanceDailyDto.builder()
                    .date(keyDate.toString())
                    .weekNumber(week)
                    .startTime(startDt.toString())
                    .endTime(endDt != null ? endDt.toString() : null)
                    .workingMinutes(minutes)
                    .status(status)
                    .statusName(statusName)
                    .remarkCd(remarkCd)
                    .remark(remark)
                    .build();

            addToWeekList(weekList, week, daily);
            cursor = cursor.plusDays(1);
        }

        summary.finalizeSummary();
        return new MonthlyAttendanceResponseDto(weekList, summary);
    }


    @Transactional(readOnly = true)
    private Map<LocalDateTime, Attendance> loadAttendanceMap(Long empId, LocalDateTime start, LocalDateTime end) {
        return attendanceRepository.findByConditions(empId, start, end)
                .stream()
                .collect(Collectors.toMap(
                        a -> a.getStartTime().toLocalDate().atStartOfDay(),
                        a -> a
                ));
    }


    @Transactional(readOnly = true)
    private Map<LocalDateTime, TblLeave> loadLeaveMap(Long empId, LocalDateTime startDate, LocalDateTime endDate) {

        List<TblLeave> leaves = leaveRepository.findActiveLeave(empId, "APPLY", startDate.toString(), endDate.toString());

        Map<LocalDateTime, TblLeave> map = new HashMap<>();

        for (TblLeave leave : leaves) {
            LocalDate s = LocalDate.parse(leave.getLeaveStartDate());
            LocalDate e = LocalDate.parse(leave.getLeaveEndDate());

            LocalDate cursor = s;
            while (!cursor.isAfter(e)) {
                map.put(cursor.atStartOfDay(), leave);
                cursor = cursor.plusDays(1);
            }
        }

        return map;
    }


    private int resolveLeaveMinutes(String type) {
        if ("AL".equals(type)) return 8 * 60;
        if ("SL".equals(type)) return 8 * 60;
        if ("EL".equals(type)) return 8 * 60;
        if ("BHL".equals(type)) return 4 * 60;
        if ("AHL".equals(type)) return 4 * 60;
        return 0;
    }

    private double resolveLeaveDay(String type) {
        if ("AL".equals(type)) return 1.0;
        if ("SL".equals(type)) return 1.0;
        if ("EL".equals(type)) return 1.0;
        if ("BHL".equals(type)) return 0.5;
        if ("AHL".equals(type)) return 0.5;
        return 0;
    }

    @Override
    @Transactional
    public GetOffAttendanceResponseDto getOff(Long attendanceId, GetOffAttendanceRequestDto request) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new CustomException(CustomMessage.NO_ATTENDANCE_FOR_CHECKOUT));

        LocalDateTime endTime = LocalDateTime.parse(request.getEndTime());
        attendance.setEndTime(endTime);

        return new GetOffAttendanceResponseDto(
                attendance.getAttendanceId(),
                endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

}
