package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pay")
@Builder
public class Pay {

    @Id
    @Column(name = "emp_id")
    private Long empId;     //PK, 사원을 기준으로

    @Column(name = "work_pay")
    private Integer workPay;

}
