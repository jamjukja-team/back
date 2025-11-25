package com.supercoding.hrms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.com.exception.ErrorResponse;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.security.util.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final EmployeeRepository employeeRepository;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        String accessToken = resolveToken(request);
        String refreshToken = resolveRefreshTokenCookie(request);

        if(path.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.equals("/api/auth/refresh")) {
            if (accessToken == null) {
                throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_REQUIRED);
            }

            try {
                jwtTokenProvider.getEmailFromExpiredToken(accessToken);

            } catch (CustomException ex) {
                // getEmailFromExpiredToken 내부에서 CustomException으로 이미 변환된 경우
                CustomMessage cm = ex.getCustomMessage();

                ErrorResponse errorResponse = new ErrorResponse(
                        cm.getHttpStatus().value(),         // status (int)
                        cm.getHttpStatus().name(),          // error (String)
                        cm.name(),                          // code (String)
                        cm.getMessage()                     // message (String)
                );

                response.setStatus(cm.getHttpStatus().value());
                response.setContentType("application/json;charset=UTF-8");

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

                return;

            } catch (Exception ex) {
                // 그 외 모든 JWT 오류
                throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_INVALID);
            }

            filterChain.doFilter(request, response);
            return;
        } //if-end

        try {
            if (refreshToken == null && !path.equals("/api/auth/login")) {
                throw new CustomException(CustomMessage.FAIL_REFRESH_TOKEN_INVALID);
            }

            if (accessToken != null) {
                String email = null;
                jwtTokenProvider.getEmailFromExpiredToken(accessToken);

                try {
                    email = jwtTokenProvider.getEmailFromToken(accessToken);
                } catch (ExpiredJwtException e) {
                    throw new CustomException(CustomMessage.FAIL_ACCESS_TOKEN_EXPIRED);
                }

                Employee employee = employeeRepository.findByEmail(email)
                        .orElseThrow(() -> new CustomException(CustomMessage.FAIL_EMAIL_NOT_FOUND));

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        //           UsernamePasswordAuthenticationToken authentication =
        //                   new UsernamePasswordAuthenticationToken(
        //                           employee, null, null);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            CustomMessage cm = ex.getCustomMessage();

            ErrorResponse errorResponse = new ErrorResponse(
                    cm.getHttpStatus().value(),         // status (int)
                    cm.getHttpStatus().name(),          // error (String)
                    cm.name(),                          // code (String)
                    cm.getMessage()                     // message (String)
            );

            response.setStatus(cm.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

            return;
        }
    }

    private String resolveRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
