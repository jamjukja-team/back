package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.PayrollDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Long> {
    List<PayrollDetail> findByPayHistId(Long payHistId);
}
