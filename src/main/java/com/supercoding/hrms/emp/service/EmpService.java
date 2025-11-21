package com.supercoding.hrms.emp.service;

import com.supercoding.hrms.com.entity.Department;
import com.supercoding.hrms.com.entity.Grade;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.com.repository.DepartmentRepository;
import com.supercoding.hrms.com.repository.GradeRepository;
import com.supercoding.hrms.com.service.MailService;
import com.supercoding.hrms.emp.dto.EmployeeSaveRequestDto;
import com.supercoding.hrms.emp.dto.EmployeeSaveResponseDto;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

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
                .empNo(employeeRepository.findMaxEmpNo())
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
        
        sendMail(employee.getEmail() ,mailService.getInitialPasswordUrl(employee.getEmail()));

        return new EmployeeSaveResponseDto(employee);
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

        System.out.println(url);

        mailService.sendMailMessage(email, subject, text);
    }
}
