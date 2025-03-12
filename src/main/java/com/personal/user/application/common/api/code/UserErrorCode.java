package com.personal.user.application.common.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

    EMAIL_CONFLICT(HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
    ;

    private final int code;
    private final String message;
}

