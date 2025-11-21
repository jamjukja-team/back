package com.supercoding.hrms.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessToken(String empId) {
        long expiration_30m = 1000L * 30;
        return Jwts.builder()
                .setSubject(empId)
                .claim("token_type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String empId) {
        long expiration_30m = 1000L * 60;
        return Jwts.builder()
                .setSubject(empId)
                .claim("token_type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }
}
