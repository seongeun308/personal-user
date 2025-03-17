package com.personal.user.application.common.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    EMAIL_CONFLICT(HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED.value(), "이메일 또는 비밀번호가 잘못 되었습니다."),
    UNEXPECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 오류가 발생했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "관리자 권한이 필요합니다.")
    ;

    private final int code;
    private final String message;
}

