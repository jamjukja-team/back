package com.supercoding.hrms.emp.repository;

import com.supercoding.hrms.emp.entity.EmpNoSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface EmpNoSequenceRepository extends JpaRepository<EmpNoSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 읽은 트랜잭션이 끝날때 까지 다른 트랜잭션은 대기
    @Query("SELECT s FROM EmpNoSequence s WHERE s.id = 1")
    EmpNoSequence getForUpdate();
}
