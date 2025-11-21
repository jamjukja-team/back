package com.supercoding.hrms.com.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "com_detail")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommonDetail {
    @Id
    @Column(name = "com_cd", length = 30)
    private String comCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "com_group_cd", nullable = false)
    private CommonGroup group;

    @Column(name = "com_nm", nullable = false, length = 100)
    private String comNm;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDate createAt;

    @Column(name = "in_emp_id", nullable = false, length = 4)
    private String inEmpId;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDate updateAt;

    @Column(name = "up_emp_id", nullable = false, length = 4)
    private String upEmpId;
}
