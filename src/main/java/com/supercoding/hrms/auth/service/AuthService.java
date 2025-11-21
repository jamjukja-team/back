package com.supercoding.hrms.auth.service;

import com.supercoding.hrms.auth.dto.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.LoginParamResponseDto;
import com.supercoding.hrms.auth.entity.Account;
import com.supercoding.hrms.auth.entity.RefreshToken;
import com.supercoding.hrms.auth.repository.AccountRepository;
import com.supercoding.hrms.auth.repository.RefreshTokenRepository;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    private void verifyPassword(String reqPassword, String accPassword) {
        if(!passwordEncoder.matches(reqPassword, accPassword)) {
            throw new CustomException(CustomMessage.FAIL_WRONG_PASSWORD);
        }

    }

}
