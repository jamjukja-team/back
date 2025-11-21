package com.supercoding.hrms.attendance.dto.request.delete;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DeleteAttendanceRequestDto {
    List<Long> ids;
}
