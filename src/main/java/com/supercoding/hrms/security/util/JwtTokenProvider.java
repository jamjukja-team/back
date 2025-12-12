package com.supercoding.hrms.security.util;

import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateAccessToken(String email) {
        long expiration_30m = 1000L * 60 * 30;
        return Jwts.builder()
                .setSubject(email)
                .claim("token_type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        long expiration_7d = 1000L * 60 * 60 * 24 * 7;
        return Jwts.builder()
                .setSubject(email)
                .claim("token_type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_7d))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("token_type", String.class);

            // 타입 검증
            if (!expectedType.equals(tokenType)) {
                throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
            }

            return true;

        } catch (ExpiredJwtException e) {

            Claims claims = e.getClaims();
            String tokenType = claims.get("token_type", String.class);

            if ("REFRESH".equals(tokenType)) {
                throw new CustomException(CustomMessage.TOKEN_EXPIRED);
            }
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);

        } catch (Exception e) {
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmailFromExpiredToken(String token) {
        try {
            // 만료 여부와 상관없이 signature 체크 + claims 파싱을 시도
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)  // 여기서 signature + expiration 검사
                    .getBody()
                    .getSubject();

        } catch (ExpiredJwtException ex) {
            // 서명은 맞고, 만료된 경우만 여기로 들어온다.
            throw new CustomException(CustomMessage.TOKEN_EXPIRED);

        } catch (SignatureException ex) {
            // 서명 불일치 → 위조된 토큰
            throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_INVALID);

        } catch (MalformedJwtException ex) {
            // 형식 자체가 이상함
            throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_INVALID);

        } catch (Exception ex) {
            // 기타 모든 오류
            throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_INVALID);
        }
    }
}
