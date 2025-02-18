package com.personal.user.application.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {

    OK(HttpStatus.OK.value(), "성공적으로 조회했습니다."),
    CREATED(HttpStatus.CREATED.value(), "등록에 성공했습니다."),
    EMAIL_CONFLICT(HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    ;

    private final int code;
    private final String message;

}

