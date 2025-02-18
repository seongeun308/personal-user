package com.personal.user.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Api<T> {
    private String result;
    private int code;
    private String message;
    private T data;

    public static <T> Api<T> ok(StatusCode statusCode, T data) {
        return Api.<T>builder()
                .result(Result.SUCCESS.getStatus())
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> Api<T> error(StatusCode statusCode, T data) {
        return Api.<T>builder()
                .result(Result.FAIL.getStatus())
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .data(data)
                .build();
    }
}
