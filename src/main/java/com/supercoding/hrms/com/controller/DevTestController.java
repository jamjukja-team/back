package com.supercoding.hrms.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DevTestController {

    private final JdbcTemplate jdbcTemplate;

    // 앱 살아있는지 체크
    @GetMapping("/dev-test/ping")
    public String ping() {
        return "app ok";
    }

    // DB 연결 되는지 체크
    @GetMapping("/dev-test/db")
    public String dbTest() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return "db ok: " + result;
    }

    // 깃 액션 체크
    @GetMapping("/dev-test/action")
    public String action() {
        return "action ok";
    }
}
