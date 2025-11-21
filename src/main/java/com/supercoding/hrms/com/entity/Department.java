package com.supercoding.hrms.com.entity;

import com.supercoding.hrms.emp.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Department {
    @Id
    @Column(name = "dept_id", length = 10)
    private String deptId;
    @Column(name= "dept_nm", nullable = false)
    private String deptNm;
    @Column(name = "use_status_cd", length = 20)
    @Builder.Default
    private String useStatusCd = "USE";

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

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
