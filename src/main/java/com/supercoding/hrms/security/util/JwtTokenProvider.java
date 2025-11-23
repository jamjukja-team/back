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
        long expiration_30m = 1000L * 60 * 60;
        return Jwts.builder()
                .setSubject(email)
                .claim("token_type", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        long expiration_30m = 1000L * 60 * 60 * 24 * 7;
        return Jwts.builder()
                .setSubject(email)
                .claim("token_type", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration_30m))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, String type) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)   // 여기서 서명 검증 + 만료 검증됨
                    .getBody();

            //== 토큰 타입 검증 ==//
            String tokenType = claims.get("token_type", String.class);

            if (!type.equals(tokenType)) {
                throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
            }

            return true;

        } catch (ExpiredJwtException e) {

            // 만료된 토큰 Claims 는 여기서 꺼낼 수 있음
            Claims claims = e.getClaims();
            String tokenType = claims.get("token_type", String.class);

            if ("ACCESS".equals(tokenType)) {
                throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_EXPIRED);
            }

            if ("REFRESH".equals(tokenType)) {
                throw new CustomException(CustomMessage.FAIL_REFRESH_TOKEN_EXPIRED);
            }

            // 타입조차 없다면 그냥 invalid
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);

        } catch (SignatureException e) {
            // 서명 위조 (key 다름 or 변조됨)
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);

        } catch (MalformedJwtException e) {
            // 형식이 이상함
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);

        } catch (IllegalArgumentException e) {
            // null, empty 등
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);

        } catch (Exception e) {
            // 기타 예외
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
