package com.supercoding.hrms.com.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Message {
    REGIST_SUCCESS(HttpStatus.CREATED, "사원등록을 완료했습니다."),
    FAIL_EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "회원가입 실패: 이미 사용 중인 이메일입니다."),
    FAIL_UNKNOWN(HttpStatus.BAD_REQUEST, "회원가입 실패: 잘못된 요청입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
