package com.supercoding.hrms.auth.service;

import com.supercoding.hrms.auth.dto.request.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.response.LoginParamResponseDto;
import com.supercoding.hrms.auth.dto.request.SetPasswordRequestDto;
import com.supercoding.hrms.auth.entity.RefreshToken;
import com.supercoding.hrms.auth.repository.RefreshTokenRepository;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.emp.dto.response.RefreshResponseDto;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginParamResponseDto login(LoginParamRequestDto request) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_EMAIL_NOT_FOUND));

        if(employee == null) {
            throw new CustomException(CustomMessage.FAIL_WRONG_EMAIL);
        }

        //비밀번호 검증
        verifyPassword(request.getPassword(), employee.getPassword());
        
        String accessToken = jwtTokenProvider.generateAccessToken(employee.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(employee.getEmail());

        //기존 refresh 토큰을 N으로 변경시킨다.
        refreshTokenRepository.revokeAllByEmpId(employee.getEmpId());

        //새로운 refresh 토큰 생성
        refreshTokenRepository.save(RefreshToken.builder().empId(employee.getEmpId()).refreshToken(refreshToken).build());

        return new LoginParamResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void setInitialPassword(SetPasswordRequestDto request) {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_EMAIL_NOT_FOUND));
        // 비밀번호 인코딩 후 저장
        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 계정 활성화 플래그 on (예시)
        employee.setAccountStatusCd("NORMAL");
    }

    private void verifyPassword(String reqPassword, String accPassword) {
        if(!passwordEncoder.matches(reqPassword, accPassword)) {
            throw new CustomException(CustomMessage.FAIL_WRONG_PASSWORD);
        }
    }

    @Transactional
    public RefreshResponseDto getToken(String refreshToken) {
        if(jwtTokenProvider.validateToken(refreshToken, "REFRESH")) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);

            return new RefreshResponseDto(jwtTokenProvider.generateAccessToken(email), null);
        }
        throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
    }

    @Transactional
    public void logout(String accessToken) {

        String email = jwtTokenProvider.getEmailFromToken(accessToken.replace("Bearer ", ""));

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_EMAIL_NOT_FOUND));

        if(employee == null) {
            throw new CustomException(CustomMessage.FAIL_WRONG_EMAIL);
        }

        refreshTokenRepository.revokeAllByEmpId(employee.getEmpId());

    }
}
