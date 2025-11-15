package com.supercoding.hrms.leave.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor //생성자 자동 주입
public class leaveController {
    // 프론트와 통신하는 역할 (CRUD 구현 -> API로 받아서 서비스한테 넘기는거)

}
