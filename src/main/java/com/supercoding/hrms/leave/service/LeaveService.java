package com.supercoding.hrms.leave.service;

import com.supercoding.hrms.com.service.CommonUploadService;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.dto.LeaveType;
import com.supercoding.hrms.leave.repository.FileRepository;
import com.supercoding.hrms.leave.repository.LeaveRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {
    // Leave 정보 CRUD 구현

    private final LeaveRepository leaveRepository;
    private final FileRepository fileRepository;
    private final CommonUploadService uploadService;

    private final EmployeeRepository employeeRepository;

    // 사원이름(empNm), 사번(empNo)을 가져오기 위함 LeaveType에 넣으려고
    private LeaveType setEmpInfo(Long empId, LeaveType leaveType){
        Optional<Employee> emp = employeeRepository.findById(empId);
        if(emp.isPresent()){
            leaveType.setEmpNm(emp.get().getEmpNm());
            leaveType.setEmpNo(emp.get().getEmpNo());
        }else{
            log.info("사원 정보가 없습니다 : {}", empId);
        }
        return leaveType;
    }

    // 1. Create
    public LeaveType create(TblLeave leave, MultipartFile file) {
        if(file != null){
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String ext = fileName.substring(fileName.lastIndexOf(".")+1);
            // upload 파일 안의 key는 leave라는 키로 설정된 폴더 안에 파일 저장함
            // 반환하는건 서버에 저장된 파일의 location 임.
            String location = uploadService.uploadFile(file, "leave");
            String fileId = random16();
            TblFile tblFile = new TblFile(
                    fileId,
                    fileName,
                    ext,
                    location
            );
            TblFile saveFile = fileRepository.save(tblFile);

            Double duration = calcDuration(leave.getLeaveStartDate(), leave.getLeaveEndDate(), leave.getLeaveType()); // 휴가 기간 계산

            leave.setFileId(fileId);
            leave.setLeaveStatus("WAIT");
            leave.setLeaveDuration(duration); // 휴가 기간 저장

            TblLeave saveLeave = leaveRepository.save(leave);

            // 잔여 연차 업데이트
            updateRemainLeave(leave.getEmpId(), duration, false);

            LeaveType leaveType = new LeaveType(saveLeave, saveFile);

            return setEmpInfo(leave.getEmpId(), leaveType);
        }else{
            leave.setLeaveStatus("WAIT");
            TblLeave saveLeave = leaveRepository.save(leave);

            LeaveType leaveType = new LeaveType(saveLeave, null);

            return setEmpInfo(leave.getEmpId(), leaveType);
        }
    }

    // 사원 정보를 한번에 불러서 남은 휴가 정보 업데이트
    @Transactional
    public void updateRemainLeave(Long empId, Double duration, Boolean isReject){
        Optional<Employee> empInfo = employeeRepository.findById(empId);
        if(empInfo.isPresent()){
            Double remainLeave = empInfo.get().getRemainLeave();
            if(!isReject){
                empInfo.get().setRemainLeave(remainLeave - duration);
            }else{
                empInfo.get().setRemainLeave(remainLeave + duration);
            }
//            employeeRepository.save(empInfo.get());  // Transactional 때문에 자동 계산.
        }else{
            log.info("사원정보가 없습니다 : {}", empId);
        }
    }

    // 잔여 연차 계산
    public Double calcDuration(String startDate, String endDate, String leaveType){

        if(leaveType.equals("AHL") || leaveType.equals("BHL")){ // 반차
            return 0.5;
        }else if(leaveType.equals("AL")){ // 연차
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            LocalDate d1 = LocalDate.parse(startDate, formatter);
            LocalDate d2 = LocalDate.parse(endDate, formatter);

            long diff = d2.toEpochDay() - d1.toEpochDay();

            return (double) diff;
        }else{ // 병가, 경조
            return 0.0;
        }
    }

    // 2. Read (단건)
    public LeaveType read(Long leaveId) {
        TblLeave tblLeave = leaveRepository.findById(leaveId).orElse(null);
        String fileId = tblLeave!=null?tblLeave.getFileId():"";
        TblFile tblFile = fileId.isEmpty()?null:fileRepository.findById(fileId).orElse(null);

        LeaveType leaveType = new LeaveType(tblLeave, tblFile);

        if(tblLeave == null){
            log.info("조회되는 휴가 정보가 없습니다");
            return leaveType;
        }else{
            return setEmpInfo(tblLeave.getEmpId(), leaveType);
        }
    }

    // 3. Read (목록)
    public List<LeaveType> readList(String startDate, String endDate, String status, Long empId, String roleType, String deptId, String gradeId, String empNm) {
        List<TblLeave> lists = leaveRepository.findByLeaveRegDateBetween(startDate, endDate);

        if(roleType.equals("USER")){
            lists = lists.stream().filter(item -> item.getEmpId().equals(empId)).toList(); // 사원이 휴가내역을 볼경우 자신의 empId를 불러와서 자신의 것만 보여주게 해야 한다.
        }else{
            if (!roleType.equals("ADMIN")){
                log.info("잘못된 유저 타입 기입");
                return Collections.emptyList();
            }
        }

        List<Employee> employeeList = employeeRepository.findAll();

        if(!deptId.isEmpty()){
            employeeList = filterByDept(deptId, employeeList);
        }

        if(!gradeId.isEmpty()){
            employeeList = filterByGrade(gradeId, employeeList);
        }

        if(!empNm.isEmpty()){
            employeeList = filterByName(empNm, employeeList);
        }

        if(!employeeList.isEmpty()){
            List<Long> empIds = employeeList.stream().map(Employee::getEmpId).toList();
            lists = lists.stream().filter(item -> empIds.contains(item.getEmpId())).toList();
        }

        if(!status.isEmpty()){ // 전체일 때는 ""(빈값)으로 보내면 됨. 아닐 때는 cd 값으로 보내기
            lists = lists.stream().filter(item -> item.getLeaveStatus().equals(status)).toList();
        }

        return lists.stream().map(list -> read(list.getLeaveId())).toList();
    }

    //전체 사원 조회 받아서 부서별 필터링
    public List<Employee> filterByDept(String deptId, List<Employee> employees){
        return employees.stream().filter(item -> item.getDepartment().getDeptId().equals(deptId)).toList();
    }

    //전체 사원 조회 받아서 직급별 필터링
    public List<Employee> filterByGrade(String gradeId, List<Employee> employees){
        return employees.stream().filter(item -> item.getGrade().getGradeId().equals(gradeId)).toList();
    }

    //전체 사원 조회 받아서 이름별 필터링
    public List<Employee> filterByName(String nameId, List<Employee> employees){
        return employees.stream().filter(item -> item.getEmpNm().equals(nameId)).toList();
    }

    // 5. Delete (단건)
    public boolean delete(Long leaveId) {
        try {
            leaveRepository.deleteById(leaveId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 6. Delete (다건)
    public boolean deleteList(List<Long> leaveIds) {
        try {
            leaveIds.forEach(leaveRepository::deleteById);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 승인 반려 처리
    public void updateStatus(Long leaveId, String reason, String status) {
        LeaveType leaveType = read(leaveId);
        leaveType.getLeaveInfo().setLeaveStatus(status);
        leaveType.getLeaveInfo().setRejectReason(reason);

        // 빠진 연차 반려로 다시 복구
        updateRemainLeave(leaveType.getLeaveInfo().getEmpId(), leaveType.getLeaveInfo().getLeaveDuration(),true);
        leaveRepository.save(leaveType.getLeaveInfo());
    }

    public String random16() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
