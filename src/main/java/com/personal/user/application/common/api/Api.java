package com.personal.user.application.common.api;

import com.personal.user.application.common.api.code.ErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Api<T> {
    private String result;
    private int code;
    private String message;
    private T data;

    public static <T> Api<T> ok(T data) {
        return Api.<T>builder()
                .result(Result.SUCCESS.getStatus())
                .code(HttpStatus.OK.value())
                .message("성공")
                .data(data)
                .build();
    }

    public static <T> Api<T> error(ErrorCode errorCode, T data) {
        return Api.<T>builder()
                .result(Result.FAIL.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> Api<T> error(HttpStatus status, String message, T data) {
        return Api.<T>builder()
                .result(Result.FAIL.getStatus())
                .code(status.value())
                .message(message)
                .data(data)
                .build();
    }
}
