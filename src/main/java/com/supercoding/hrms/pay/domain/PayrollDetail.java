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

    @Column(name = "pay_hist_id")   // Payroll과의 FK 값
    private Long payHistId;

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "item_cd")
    private String itemCd;          // 예: "B001", "D002" 등 항목 코드

    @Column(name = "item_nm")
    private String itemNm;          // 예: "기본급", "식대" 등 항목 이름

    @Column(name = "amount")
    private Integer amount;         // 금액

    @Column(name = "remark")
    private String remark;          // 비고

}
