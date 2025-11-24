package com.supercoding.hrms.workhour.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workhour")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Workhour {
    @Id
    private Long workhourId;

    private Long empId;


    @Column(nullable = false)
    private Long hour;

    @Column(nullable = false)
    private Long minute;

    @Column(nullable = false)
    private Boolean isOvertime = false;

    private Long overtimeHour;

    private Long overtimeMinute;
}
