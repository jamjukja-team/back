package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.PayrollDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Long> {
}
