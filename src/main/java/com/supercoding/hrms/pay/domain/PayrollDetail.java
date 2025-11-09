package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payroll_detail")
public class PayrollDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_detail_id")
    private Long payrollDetailId;

    @Column(name = "amount")
    private Integer amount;   // 금액
    @Column(name = "remark")
    private String remark;    // 비고
    @Column(name = "empId")
    private Long empId;       // 직원 ID

}
