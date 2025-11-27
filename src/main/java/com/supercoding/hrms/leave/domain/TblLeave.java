package com.supercoding.hrms.leave.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_leave")
@Builder
public class TblLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 증가해서 생성
    @Column(name = "leave_id")
    private Long leaveId; // 연차 id (pk)

    @Column(name = "leave_reg_date")
    private String leaveRegDate; // 휴가 신청 날짜

    @Column(name = "leave_start_date")
    private String leaveStartDate; // 휴가 시작 날짜

    @Column(name = "leave_end_date")
    private String leaveEndDate; //휴가 끝 날짜

    @Column(name = "leave_type")
    private String leaveType; //휴가 타입(종류),{연차, 반차, 병가} -> AHL, BHL

    @Column(name = "leave_reason")
    private String leaveReason; //사유

    @Column(name = "file_id")
    private String fileId; // 첨부파일 id

    @Column(name = "leave_duration")
    private int leaveDuration; //휴가 기간

    @Column(name = "leave_status")
    private String leaveStatus; //상태

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "emp_Id", nullable = false)
//    private Employee employee; //사번

    private Long empId;

    @Column(name = "reject_reason")
    private String rejectReason; // 반려사유

}
