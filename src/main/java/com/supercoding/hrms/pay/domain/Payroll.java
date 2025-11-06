package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payroll")
@Getter @Setter
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payHistId;

    private Long empId;

    @Column(length = 7)
    private String payMonth;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    private LocalDate payDate;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL)
    private List<PayrollDetail> details;
}
