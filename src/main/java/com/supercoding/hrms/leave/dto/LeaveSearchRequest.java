package com.supercoding.hrms.leave.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveSearchRequest {
    private String deptFilterCd;
    private String gradeFilterCd;
    private String keyword;
}
