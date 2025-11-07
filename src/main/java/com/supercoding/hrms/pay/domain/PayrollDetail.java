package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"payroll", "item"}) // 🔹 무한루프 방지
@Entity
@Table(name = "payroll_detail")
public class PayrollDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollDetailId;

    private Integer amount;   // 금액
    private String remark;    // 비고
    private Long empId;       // 직원 ID

    // ✅ 급여 이력과의 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_hist_id")
    private Payroll payroll;

    // ✅ 급여 항목(ItemNm)과의 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd")
    private ItemNm item;

}
