package com.supercoding.hrms.emp.service;

import com.supercoding.hrms.com.entity.CommonDetail;
import com.supercoding.hrms.com.entity.Department;
import com.supercoding.hrms.com.entity.Grade;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.com.repository.CommonDetailRepository;
import com.supercoding.hrms.com.repository.DepartmentRepository;
import com.supercoding.hrms.com.repository.GradeRepository;
import com.supercoding.hrms.com.service.CommonMailService;
import com.supercoding.hrms.emp.dto.request.EmployeeSaveRequestDto;
import com.supercoding.hrms.emp.dto.request.EmployeeSearchRequestDto;
import com.supercoding.hrms.emp.dto.response.*;
import com.supercoding.hrms.emp.entity.EmpNoSequence;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmpNoSequenceRepository;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.security.util.JwtTokenProvider;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;
    private final CommonDetailRepository commonDetailRepository;
    private final EmpNoSequenceRepository empNoSequenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonMailService commonMailService;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public EmployeeSaveResponseDto saveEmployee(EmployeeSaveRequestDto req) {

        if(employeeRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(CustomMessage.FAIL_EMAIL_EXISTS);
        }

        if(req.getRegisterId() == null) {
            throw new CustomException(CustomMessage.FAIL_UNKNOWN);
        }

        Department dept = departmentRepository.getReferenceById(req.getDeptId());
        Grade grade = gradeRepository.getReferenceById(req.getGradeId());


        Employee employee = employeeRepository.save(Employee.builder()
                .email(req.getEmail())
                .empNo(generateEmpNo())
                .empNm(req.getEmpNm())
                .password(passwordEncoder.encode(req.getPassword()))
                .birthDate(req.getBirthDate())
                .hireDate(req.getHireDate())
                .phone(req.getPhone())
                .photo(req.getPhoto())
                .department(dept)
                .grade(grade)
                .inEmpId(req.getRegisterId())
                .upEmpId(req.getRegisterId())
                .build());
        
        sendMail(employee.getEmail() , commonMailService.getInitialPasswordUrl(employee.getEmail()));

        return new EmployeeSaveResponseDto(employee);
    }

    @Transactional
    private Long generateEmpNo() {
        EmpNoSequence seq = empNoSequenceRepository.getForUpdate();
        Long next = seq.getNextNo();
        seq.setNextNo(next + 1);
        return next;
    }


    private void sendMail(String email, String url) {
        String subject = "[HRMS] 계정 생성 안내";
        String text = """
            안녕하세요.
            HRMS 계정이 생성되었습니다.

            아래 링크에서 비밀번호를 설정하세요:
            %s

            감사합니다.
            """.formatted(url);

        commonMailService.sendMailMessage(email, subject, text);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeSearchResponseDto> searchEmployees(EmployeeSearchRequestDto request, Pageable pageable) {
        Specification<Employee> spec = buildSearchSpec(request);
        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        // EMP_STATUS 그룹의 코드 목록을 조회한다.
        List<CommonDetail> codeList = commonDetailRepository
                .findByGroup_ComGroupCdOrderByComCdAsc("EMP_STATUS");

        // 코드(comCd) → 코드명(comNm) 으로 Map 변환
        Map<String, String> empStatusNameMap = codeList.stream()
                .collect(Collectors.toMap(
                        CommonDetail::getComCd,   // Key
                        CommonDetail::getComNm    // Value
                ));

        return page.map(emp -> new EmployeeSearchResponseDto(
                emp.getEmpId(),
                emp.getEmpNo(),
                emp.getEmpNm(),
                emp.getDepartment() != null ? emp.getDepartment().getDeptNm() : null,
                emp.getGrade() != null ? emp.getGrade().getGradeNm() : null,
                empStatusNameMap.getOrDefault(emp.getEmpStatusCd(), null),
                emp.getAccountStatusCd(),
                emp.getHireDate()
        ));
    }

    private Specification<Employee> buildSearchSpec(EmployeeSearchRequestDto request) {
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
            
            //재직상태 조건
            if (request.getEmpStatusCd() != null && !"EMP_STATUS_ALL".equals(request.getEmpStatusCd())) {
                predicates.add(
                        cb.equal(root.get("empStatusCd"), request.getEmpStatusCd())
                );
            }

            //계정 상태 조건
            if (request.getAccountStatusCd() != null && !"ACCOUNT_STATUS_ALL".equals(request.getAccountStatusCd())) {
                predicates.add(
                        cb.equal(root.get("accountStatusCd"), request.getAccountStatusCd())
                );
            }
            
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

    @Transactional(readOnly = true)
    public EmployeeDetailResponseDto getEmployeeByAdmin(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_USER_NOT_FOUND));

        return new EmployeeDetailResponseDto(
                employee.getPhoto(),
                employee.getEmpId(),
                employee.getEmpNo(),
                employee.getDepartment().getDeptId(),
                employee.getDepartment().getDeptNm(),
                employee.getGrade().getGradeId(),
                employee.getGrade().getGradeNm(),
                employee.getEmpNm(),
                employee.getHireDate(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getAccountStatusCd(),
                employee.getEmpStatusCd()
        );
    }

    @Transactional
    public void getAccLock(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() ->  new CustomException(CustomMessage.FAIL_USER_NOT_FOUND));

        CommonDetail disableCode = (CommonDetail) commonDetailRepository
                .findByGroup_ComGroupCdAndComCd("ACCOUNT_STATUS", "LOCK")
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_ACCOUNT_STATUS_CODE_NOT_FOUND));


        employee.setAccountStatusCd(disableCode.getComCd());

    }

    @Transactional
    public void getAccUnlock(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() ->  new CustomException(CustomMessage.FAIL_USER_NOT_FOUND));

        CommonDetail disableCode = (CommonDetail) commonDetailRepository
                .findByGroup_ComGroupCdAndComCd("ACCOUNT_STATUS", "NORMAL")
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_ACCOUNT_STATUS_CODE_NOT_FOUND));


        employee.setAccountStatusCd(disableCode.getComCd());
    }
}