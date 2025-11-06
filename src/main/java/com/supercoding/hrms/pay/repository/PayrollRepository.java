package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
}
