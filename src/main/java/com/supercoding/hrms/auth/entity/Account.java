package com.supercoding.hrms.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Immutable
@Subselect("""
    SELECT e.emp_id   AS emp_id,
           e.email    AS email,
           e.password AS password
    FROM hrms.employee e
""")
@Synchronize("employee")
@Getter
public class Account {
    @Id
    @Column(name = "emp_id")
    private Long empId;

    private String email;
    private String password;
}
