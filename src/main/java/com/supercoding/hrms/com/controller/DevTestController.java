package com.supercoding.hrms.com.controller;

import com.supercoding.hrms.com.service.CommonUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DevTestController {

    private final JdbcTemplate jdbcTemplate;
    private final CommonUploadService commonUploadService;

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
        return "우왕 성공이다.";
    }
    
    //파일 업로드 테스트
    @PostMapping("/api/common/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws FileUploadException {
        String url = commonUploadService.uploadFileTest(file, "employee");
        return ResponseEntity.ok(url);
    }
}
