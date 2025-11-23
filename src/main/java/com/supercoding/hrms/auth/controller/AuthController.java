package com.supercoding.hrms.auth.controller;

import com.supercoding.hrms.auth.dto.response.AccessTokenResponseDto;
import com.supercoding.hrms.auth.dto.request.LoginParamRequestDto;
import com.supercoding.hrms.auth.dto.response.LoginParamResponseDto;
import com.supercoding.hrms.auth.dto.request.SetPasswordRequestDto;
import com.supercoding.hrms.auth.service.AuthService;
import com.supercoding.hrms.com.exception.CustomException;
import com.supercoding.hrms.com.exception.CustomMessage;
import com.supercoding.hrms.emp.dto.response.RefreshResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/refresh")
    public RefreshResponseDto refresh(HttpServletRequest request) {
        String refreshToken = null;
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if(refreshToken == null) {
            throw new CustomException(CustomMessage.FAIL_TOKEN_INVALID);
        }

        return authService.getToken(refreshToken);
    }

    @PostMapping("/logout")
    public CustomException logout(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        authService.logout(accessToken);

        // 쿠키 삭제
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        return new CustomException(CustomMessage.SUCCESS_LOGOUT);
    }


}
