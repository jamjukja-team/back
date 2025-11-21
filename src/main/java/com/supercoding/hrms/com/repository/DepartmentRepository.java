package com.supercoding.hrms.com.repository;

import com.supercoding.hrms.com.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Iterable<Department> findByUseStatusCdOrderByDeptIdAsc(String enable);
}

