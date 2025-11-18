package com.supercoding.hrms.com.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String generateAccessToken(String email) {
        long expiration_30m = 1000L * 60 * 60;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }

    public TokenWithMeta generateRefreshToken(String email) {
        long expiration_7d = 1000L * 60 * 60 * 24 * 7;
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expiration_7d);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
        return new TokenWithMeta(refreshToken, now, exp);
    }
}
