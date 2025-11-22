package com.supercoding.hrms.auth.controller;

import com.supercoding.hrms.auth.dto.response.AccessTokenResponseDto;
import com.supercoding.hrms.auth.dto.request.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.response.LoginParamResponseDto;
import com.supercoding.hrms.auth.dto.request.SetPasswordRequestDto;
import com.supercoding.hrms.auth.service.AuthService;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/set-password")
    public ResponseEntity<CustomException> setInitialPassword(@RequestBody SetPasswordRequestDto request) {
        authService.setInitialPassword(request);
        return ResponseEntity.ok(new CustomException(CustomMessage.SUCCESS_PASSWORD_RESET));
    }
}
