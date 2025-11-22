package com.supercoding.hrms.emp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "emp_no_sequence")
public class EmpNoSequence {

    @Id
    @Column(name = "seq_id")
    private Long id;
    @Column(name = "next_no")
    private Long nextNo;
}
