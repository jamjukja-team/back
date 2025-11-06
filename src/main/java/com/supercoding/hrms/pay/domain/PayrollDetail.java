package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payroll_detail")
@Getter @Setter
public class PayrollDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollDetailId;

    private Integer amount;

    private String remark;

    private Long empId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_hist_id")
    private Payroll payroll;
}
