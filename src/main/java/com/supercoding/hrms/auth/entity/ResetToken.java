package com.supercoding.hrms.auth.entity;

import com.supercoding.hrms.emp.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ret_id")
    private Long retId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "ret_token", nullable = false, length = 128)
    private String resetToken;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used", nullable = false)
    private Boolean used;

    public ResetToken(String token, Employee employee, LocalDateTime expiresAt) {
        this.employee = employee;
        this.resetToken = token;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    public void markUsed() {
        this.used = true;
    }
}