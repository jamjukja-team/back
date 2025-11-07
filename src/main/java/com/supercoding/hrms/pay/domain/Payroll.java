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
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 증가해서 생성
    private Long payHistId;

    private Long empId;
    private String payMonth; // ex) "2025.10"

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    private LocalDate payDate;

    /*
    OneToMany는 1:N관계 즉, 한번의 급여 이력 안에 여러개의 급여 항목이 들어간다.
    mappedBy = "payroll"은 연관관계의 주인이 아니다. 즉, Payroll이 아니라 PayrollDetail 쪽이 FK(pay_hist_id)를 갖고 있다
    cascade는 payroll(부모)이 하는 행동은 payrollDetail(자식)도 똑같이 함
    FetchType.LAZY는 지연로딩이라고 하고 부모만 먼저 조회, 자식은 나중에 접근할 때 쿼리 실행
    */
    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PayrollDetail> details;
}
