package com.supercoding.hrms.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "payroll_item")
public class PayrollItem {
    @Id
    @Column(name = "cd")
    private String cd; // 항목이름의 id : "BASIC"", "MEAL", "TAX"

    @Column(name = "nm")
    private String nm;
}
