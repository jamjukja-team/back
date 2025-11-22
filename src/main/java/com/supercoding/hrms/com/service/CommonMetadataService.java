package com.supercoding.hrms.com.service;

import com.supercoding.hrms.com.dto.meta.CommonDetailDto;
import com.supercoding.hrms.com.dto.meta.DepartmentDto;
import com.supercoding.hrms.com.dto.meta.GradeDto;
import com.supercoding.hrms.com.dto.response.CommonMetaDataResponseDto;
import com.supercoding.hrms.com.entity.CommonDetail;
import com.supercoding.hrms.com.entity.Department;
import com.supercoding.hrms.com.entity.Grade;
import com.supercoding.hrms.com.repository.CommonDetailRepository;
import com.supercoding.hrms.com.repository.DepartmentRepository;
import com.supercoding.hrms.com.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonMetadataService {
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;
    private final CommonDetailRepository commonDetailRepository;

    public CommonMetaDataResponseDto getMetaData() {
        return new CommonMetaDataResponseDto(getDepartments(), getGrades(), getAllCommonCodes());
    }

    public List<Department> ListDepartments() {
        return departmentRepository.findAll();
    }

    public List<Grade> ListGrades() {
        return gradeRepository.findAll();
    }

    public List<CommonDetail> ListCodes(String groupCd) {
        return commonDetailRepository.findByGroup_ComGroupCdOrderByComCdAsc(groupCd);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDto> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> new DepartmentDto(
                        department.getDeptId(),
                        department.getDeptNm()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GradeDto> getGrades() {
        return gradeRepository.findAll().stream()
                .map(grade -> new GradeDto(
                        grade.getGradeId(),
                        grade.getGradeNm()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommonDetailDto> getAllCommonCodes() {
        return commonDetailRepository.findAll().stream()
                .map(c -> new CommonDetailDto(
                        c.getGroup().getComGroupCd(), // FK로 묶였을 때
                        c.getComCd(),
                        c.getComNm()
                ))
                .toList();
    }

}
