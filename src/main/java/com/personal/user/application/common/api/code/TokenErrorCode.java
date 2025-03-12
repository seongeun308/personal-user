package com.personal.user.application.common.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "토큰이 존재하지 않습니다."),
    TOKEN_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "요청에 토큰이 존재하지 않습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED.value(), "올바르지 않은 토큰 형식입니다."),
    INVALID_TOKEN_VALUE(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다.")
    ;

    private final int code;
    private final String message;
}
