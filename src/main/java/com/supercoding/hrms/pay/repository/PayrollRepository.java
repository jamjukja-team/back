package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    // JpaRepository 덕분에 기본 CRUD 모두 제공됨
    // create → save()
    // read → findById(), findAll()
    // update → save()
    // delete → deleteById(), deleteAllById()
}
