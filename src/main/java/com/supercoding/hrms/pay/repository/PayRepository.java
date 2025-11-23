package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
