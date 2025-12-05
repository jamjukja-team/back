package com.supercoding.hrms.auth.repository;

import com.supercoding.hrms.auth.entity.RefreshToken;
import com.supercoding.hrms.auth.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<Object> findByResetToken(String token);
}
