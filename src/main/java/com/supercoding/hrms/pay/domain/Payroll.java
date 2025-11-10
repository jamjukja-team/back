package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "details") // 🔹 무한루프 방지
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
    @Column(name = "pay_month")
    private String payMonth; // ex) "2025.10"

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private String status;

    @Column(name = "pay_date")
    private String payDate;

}
