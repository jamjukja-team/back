package com.supercoding.hrms.com.dto.response;

import com.supercoding.hrms.com.dto.meta.CommonDetailDto;
import com.supercoding.hrms.com.dto.meta.DepartmentDto;
import com.supercoding.hrms.com.dto.meta.GradeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonMetaDataResponseDto {
    private List<DepartmentDto> departments;
    private List<GradeDto> grades;
    private List<CommonDetailDto> commonCodes;
}
