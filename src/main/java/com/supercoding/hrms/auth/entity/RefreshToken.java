package com.supercoding.hrms.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tok_id")
    private Long tokId;
    @Column(name = "emp_id", nullable = false)
    private Long empId;
    @Column(name = "refresh_token", nullable = false, length = 500)
    private String refreshToken;
    @Column(nullable = false, length = 1, columnDefinition = "VARCHAR(1) CHECK (revoked IN ('Y','N')) DEFAULT 'N'")
    @Builder.Default
    private String revoked = "N";
}
