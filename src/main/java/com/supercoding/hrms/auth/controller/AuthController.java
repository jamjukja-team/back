package com.supercoding.hrms.auth.controller;

import com.supercoding.hrms.auth.dto.AccessTokenResponseDto;
import com.supercoding.hrms.auth.dto.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.LoginParamResponseDto;
import com.supercoding.hrms.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDto> login(@RequestBody @Valid LoginParamRequestDto req, HttpServletResponse response) {
        LoginParamResponseDto tokens = authService.login(req);

        Cookie refreshCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);//7일
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new AccessTokenResponseDto(tokens.getAccessToken()));
    }
}
