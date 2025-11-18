package com.supercoding.hrms.auth.controller;

import com.supercoding.hrms.auth.dto.LoginRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class LoginController {
    @PostMapping("/login")
    public String getEmployeeLoginInfo(@RequestBody @Valid LoginRequestDto req) {

        return "test";
    }
}
