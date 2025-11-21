package com.supercoding.hrms.com.repository;

import com.supercoding.hrms.com.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, String> {
    Iterable<Grade> findByUseStatusCdOrderByGradeLevelAsc(String enable);
}
