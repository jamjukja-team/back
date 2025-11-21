package com.supercoding.hrms.auth.repository;

import com.supercoding.hrms.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE RefreshToken a
           SET a.revoked = 'Y'
         WHERE a.empId = :empId
           AND a.revoked = 'N'
    """)
    int revokeAllByEmpId(@Param("empId") Long empId);
}
