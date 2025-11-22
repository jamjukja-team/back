package com.supercoding.hrms.com.repository;


import com.supercoding.hrms.com.entity.CommonDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommonDetailRepository extends JpaRepository<CommonDetail, String> {
    List<CommonDetail> findByGroup_ComGroupCd(String comGroupCd);

    List<CommonDetail> findByGroup_ComGroupCdOrderByComCdAsc(String comGroupCd);

    Optional<Object> findByGroup_ComGroupCdAndComCd(String accountStatus, String disable);
}
