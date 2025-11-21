package com.supercoding.hrms.com.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "com_group")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommonGroup {
    @Id
    @Column(name = "com_group_cd", length = 30)
    private String comGroupCd;

    @Column(name = "group_nm", nullable = false, length = 100)
    private String groupNm;

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

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<CommonDetail> details;
}
