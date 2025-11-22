package com.supercoding.hrms.auth.service;

import com.supercoding.hrms.auth.dto.request.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.response.LoginParamResponseDto;
import com.supercoding.hrms.auth.dto.request.SetPasswordRequestDto;
import com.supercoding.hrms.auth.entity.Account;
import com.supercoding.hrms.auth.entity.RefreshToken;
import com.supercoding.hrms.auth.repository.AccountRepository;
import com.supercoding.hrms.auth.repository.RefreshTokenRepository;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
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
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginParamResponseDto login(LoginParamRequestDto req) {
        Account account = accountRepository.findByEmail(req.getEmail());

        if(account == null) {
            throw new CustomException(CustomMessage.FAIL_WRONG_EMAIL);
        }

        //비밀번호 검증
        verifyPassword(req.getPassword(), account.getPassword());
        
        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(account.getEmpId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(account.getEmpId()));

        refreshTokenRepository.save(RefreshToken.builder().empId(account.getEmpId()).refreshToken(refreshToken).build());

        return new LoginParamResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void setInitialPassword(SetPasswordRequestDto request) {
        Employee employee = (Employee) employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(CustomMessage.FAIL_EMAIL_NOT_FOUND));

        System.out.println(request.getNewPassword());
        // 비밀번호 인코딩 후 저장
        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 계정 활성화 플래그 on (예시)
        employee.setAccountStatusCd("ENABLE");
    }

    private void verifyPassword(String reqPassword, String accPassword) {
        System.out.println("=========");
        System.out.println(passwordEncoder.matches(reqPassword, "$2a$10$TuZ3/ORqJ5SbG6HevBW0SeTLxwQSRtj2kCbj0cWwDVkGPDLbrkgzm"));
        System.out.println(new BCryptPasswordEncoder().encode("Test1234!"));

        String encoded = passwordEncoder.encode(reqPassword);
        System.out.println("ENCODED PASSWORD = [" + encoded + "]");



        System.out.println("=========");
        if(!passwordEncoder.matches(reqPassword, accPassword)) {
            throw new CustomException(CustomMessage.FAIL_WRONG_PASSWORD);
        }

    }
}
