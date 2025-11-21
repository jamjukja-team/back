package com.supercoding.hrms.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ResetToken {
    @Id
    @Column(name = "token_id")
    private String id;

    private Long userId;

    @Column(unique = true)
    private String token;

    private LocalDateTime expiresAt;

    private boolean used;

    public static ResetToken create(Long userId) {
        ResetToken rt = new ResetToken();
        rt.id = UUID.randomUUID().toString();
        rt.userId = userId;
        rt.token = UUID.randomUUID().toString(); // 진짜 토큰
        rt.expiresAt = LocalDateTime.now().plusMinutes(30);
        rt.used = false;
        return rt;
    }
}
