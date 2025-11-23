package com.supercoding.hrms.leave.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_leave_type")
@Builder
public class TblLeaveCommonCode {
    // 연차 종류와 연차 상태를 공통으로 관리함.
    @Id
    @Column(name = "cd")
    private String cd; // BHL, AHL, APPLY

    @Column(name = "nm")
    private String nm; // 오전반차, 오후반차, 승인

    @Column(name = "grp_cd")
    private String grpCd; // 연차 종류쪽이면 leave_type, 연차 상태쪽이면 leave_status 로 분류하는 기능
}
