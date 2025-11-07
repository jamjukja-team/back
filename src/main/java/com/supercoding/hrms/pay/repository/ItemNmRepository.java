package com.supercoding.hrms.pay.repository;

import com.supercoding.hrms.pay.domain.ItemNm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemNmRepository extends JpaRepository<ItemNm, String> {
}
