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

       try {
            if (accessToken != null) {
                String email = jwtTokenProvider.getEmailFromExpiredToken(accessToken);

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

        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
