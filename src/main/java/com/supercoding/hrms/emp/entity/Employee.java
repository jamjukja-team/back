package com.supercoding.hrms.emp.entity;

import com.supercoding.hrms.com.entity.Department;
import com.supercoding.hrms.com.entity.Grade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

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

}
