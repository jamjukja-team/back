package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payroll")
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 증가해서 생성
    @Column(name = "pay_hist_id")
    private Long payHistId;

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "status")
    private String status;

    @Column(name = "pay_date")
    private String payDate;

}
