package com.supercoding.hrms.com.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomMessage {
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃이 성공적으로 완료되었습니다."),
    SUCCESS_ACCOUNT_DISABLED(HttpStatus.OK, "계정이 비활성화되었습니다."),
    SUCCESS_ACCOUNT_ENABLE(HttpStatus.OK, "계정이 활성화되었습니다."),
    SUCCESS_PASSWORD_RESET(HttpStatus.OK, "비밀번호가 성공적으로 변경되었습니다."),


    FAIL_EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "회원가입 실패: 이미 사용 중인 이메일입니다."),
    FAIL_UNKNOWN(HttpStatus.BAD_REQUEST, "회원가입 실패: 잘못된 요청입니다."),
    FAIL_WRONG_EMAIL(HttpStatus.UNAUTHORIZED, "로그인 실패: 등록되지 않은 사원입니다."),
    FAIL_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "로그인 실패: 비밀번호가 올바르지 않습니다."),
    FAIL_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    FAIL_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "요청하신 사용자가 존재하지 않습니다."),
    FAIL_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "서버에 저장된 리프레시 토큰과 다릅니다."),
    FAIL_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    FAIL_ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    FAIL_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    FAIL_ACCOUNT_STATUS_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "계정 비활성화 코드(ACCOUNT_STATUS/DISABLE)를 찾을 수 없습니다."),
    FAIL_EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 이메일의 사용자를 찾을 수 없습니다.");




    private final HttpStatus httpStatus;
    private final String message;

}
