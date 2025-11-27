package com.supercoding.hrms.leave.service;

import com.supercoding.hrms.com.service.CommonUploadService;
import com.supercoding.hrms.emp.dto.response.EmployeeSearchResponseDto;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.domain.TblLeave;
import com.supercoding.hrms.leave.dto.LeaveListResponse;
import com.supercoding.hrms.leave.dto.LeaveSearchRequest;
import com.supercoding.hrms.leave.dto.LeaveType;
import com.supercoding.hrms.leave.repository.FileRepository;
import com.supercoding.hrms.leave.repository.LeaveRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {
    // Leave 정보 CRUD 구현

    private final LeaveRepository leaveRepository;
    private final FileRepository fileRepository;
    private final CommonUploadService uploadService;

    private final EmployeeRepository employeeRepository;

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

            leave.setFileId(fileId);
            leave.setLeaveStatus("WAIT");
            TblLeave saveLeave = leaveRepository.save(leave);

            return new LeaveType(saveLeave, saveFile);
        }else{
            leave.setLeaveStatus("WAIT");
            TblLeave saveLeave = leaveRepository.save(leave);
            return new LeaveType(saveLeave, null);
        }
    }

    // 2. Read (단건)
    public LeaveType read(Long leaveId) {
        TblLeave tblLeave = leaveRepository.findById(leaveId).orElse(null);
        String fileId = tblLeave!=null?tblLeave.getFileId():"";
        TblFile tblFile = fileId.isEmpty()?null:fileRepository.findById(fileId).orElse(null);

        return new LeaveType(tblLeave, tblFile);
    }

    // 3. Read (목록)
    public List<LeaveType> readList(String startDate, String endDate, String status, Long empId, String roleType, String deptId, String gradeId, String empNm) {
        List<TblLeave> lists = leaveRepository.findByLeaveRegDateBetween(startDate, endDate);

        if(roleType.equals("USER")){
            lists = lists.stream().filter(item -> item.getEmployee().getEmpId().equals(empId)).toList(); // 사원이 휴가내역을 볼경우 자신의 empId를 불러와서 자신의 것만 보여주게 해야 한다.
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
            lists = lists.stream().filter(item -> empIds.contains(item.getEmployee().getEmpId())).toList();
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

    public Page<LeaveListResponse> searchLeaves(LeaveSearchRequest requestDto, Pageable pageable) {
        Specification<TblLeave> spec = buildSearchSpec(requestDto);
        Page<TblLeave> page = leaveRepository.findAll(spec, pageable);

        return page.map(tblLeave -> new LeaveListResponse(
                tblLeave.getEmployee().getEmpId(),                         // empId
                tblLeave.getEmployee().getEmpNo(),                         // empNo
                tblLeave.getEmployee().getEmpNm(),                        // empNm
                tblLeave.getEmployee().getDepartment().getDeptNm(),        // deptNm
                tblLeave.getLeaveDuration(),                               // leaveDuration
                tblLeave.getEmployee().getGrade().getGradeNm(),            // gradeNm
                tblLeave.getEmployee().getHireDate()                       // hireDate
        ));
    }

    private Specification<TblLeave> buildSearchSpec(LeaveSearchRequest request) {
        return (root, query, cb) -> {
            //해당 조건을 만족하는 데이터만 가지고 오기
            List<Predicate> predicates = new ArrayList<>();

            //부서 조건
            if(request.getDeptFilterCd() != null && !"DEPT_ALL".equals(request.getDeptFilterCd())) {
                predicates.add(//조건 추가
                        cb.equal(root.get("department").get("deptId"), request.getDeptFilterCd())
                );
            }

            //직급 조건
            if (request.getGradeFilterCd() != null && !"GRADE_ALL".equals(request.getGradeFilterCd())) {
                predicates.add(
                        cb.equal(root.get("grade").get("gradeId"), request.getGradeFilterCd())
                );
            }

//            //재직상태 조건
//            if (request.getEmpStatusCd() != null && !"EMP_STATUS_ALL".equals(request.getEmpStatusCd())) {
//                predicates.add(
//                        cb.equal(root.get("empStatusCd"), request.getEmpStatusCd())
//                );
//            }
//
//            //계정 상태 조건
//            if (request.getAccountStatusCd() != null && !"ACCOUNT_STATUS_ALL".equals(request.getAccountStatusCd())) {
//                predicates.add(
//                        cb.equal(root.get("accountStatusCd"), request.getAccountStatusCd())
//                );
//            }

            //키워드 필터
            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().trim() + "%";
                predicates.add(
                        cb.or(
                                cb.like(root.get("empNm"), keyword),
                                cb.like(root.get("email"), keyword),
                                cb.like(root.get("empNo").as(String.class), keyword)
                        )
                );
            }

            //predicate 생성
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
