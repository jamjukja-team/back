package com.supercoding.hrms.emp.entity;

import com.supercoding.hrms.attendance.domain.Attendance;
import com.supercoding.hrms.com.entity.Department;
import com.supercoding.hrms.com.entity.Grade;
import com.supercoding.hrms.emp.dto.request.EmployeeUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employee", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_emp_no", columnNames = {"emp_no"}),
        @UniqueConstraint(name = "uk_employee_email", columnNames = {"email"})
})
@Getter
@Builder
@Setter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "emp_no", nullable = false, length = 4)
    private Long empNo;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name= "emp_nm", nullable = false, length = 50)
    private String empNm;

    @Column(name= "birth_date", nullable = false)
    private String birthDate;

    @Column(name= "hire_date", nullable = false, length = 10)
    private String hireDate;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @OneToMany(mappedBy = "employee")
    private List<Attendance> attendanceList;

    @Builder.Default
    @Column(name = "role_cd", length = 10, columnDefinition = "varchar(10) default 'USER'")
    private String roleCd = "USER";

    @Column(name = "account_status_cd", length = 20)
    @Builder.Default
    private String accountStatusCd = "DORMANT";

    @NotNull
    @Column(nullable = false)
    private String photo;

    @Column(name = "emp_status_cd", length = 20)
    @Builder.Default
    private String empStatusCd = "ACTIVE";

    @CreationTimestamp
    @Column(name= "create_at", updatable = false)
    private LocalDate createAt;

    @Column(name= "in_emp_id", nullable = false, length = 4)
    private String inEmpId;

    @UpdateTimestamp
    @Column(name= "update_at")
    private LocalDate updateAt;

    @Column(name= "up_emp_id",nullable = false, length = 4)
    private String upEmpId;

    @Column(name = "remain_leave")
    private Double remainLeave;

    @Column(name = "exit_reason", length = 200)
    private String exitReason;

    public void updateEmployeeInfo(EmployeeUpdateRequestDto req) {
        if (req.getEmail() != null && !req.getEmail().isBlank() && !req.getEmail().equals(this.email)) {
            this.email = req.getEmail();
        }

        if (req.getEmpNm() != null && !req.getEmpNm().isBlank() && !req.getEmpNm().equals(this.empNm)) {
            this.empNm = req.getEmpNm();
        }

        if (req.getBirthDate() != null && !req.getBirthDate().isBlank() && !req.getBirthDate().equals(this.birthDate)) {
            this.birthDate = req.getBirthDate();
        }

        if (req.getPhone() != null && !req.getPhone().isBlank() && !req.getPhone().equals(this.phone)) {
            this.phone = req.getPhone();
        }

        if (req.getRegisterId() != null && !req.getRegisterId().isBlank() && !req.getRegisterId().equals(this.upEmpId)) {
            this.upEmpId = req.getRegisterId();
        }

        this.updateAt = LocalDate.now();
    }

    public void updateDepartment(Department department) {
        this.department = department;
    }

    public void updateGrade(Grade grade) {
        this.grade = grade;
    }

    public void updatePhoto(String photo) {
        this.photo = photo;
    }

    public void retire(String reason, String registerId) {
        this.empStatusCd = "RETIRED";
        this.exitReason = reason;
        this.upEmpId = registerId;
    }
}
