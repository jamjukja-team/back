package com.supercoding.hrms.workhour.domain;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workhourId;

    @Column(nullable = false)
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
