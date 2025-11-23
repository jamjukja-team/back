package com.supercoding.hrms.leave.dto;

import com.supercoding.hrms.leave.domain.TblFile;
import com.supercoding.hrms.leave.domain.TblLeave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

    private TblLeave leaveInfo;
    private TblFile fileInfo;

}
