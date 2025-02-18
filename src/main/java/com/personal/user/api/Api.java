package com.personal.user.api;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Api<T> {
    private String result;
    private int code;
    private List<String> messages;
    private T data;

    public static <T> Api<T> ok(StatusCode statusCode, T data) {
        return Api.<T>builder()
                .result(Result.SUCCESS.getStatus())
                .code(statusCode.getCode())
                .messages(List.of(statusCode.getMessage()))
                .data(data)
                .build();
    }

    public static <T> Api<T> error(StatusCode statusCode, T data) {
        return Api.<T>builder()
                .result(Result.FAIL.getStatus())
                .code(statusCode.getCode())
                .messages(List.of(statusCode.getMessage()))
                .data(data)
                .build();
    }

    public static <T> Api<T> error(HttpStatus status, List<String> messages, T data) {
        return Api.<T>builder()
                .result(Result.FAIL.getStatus())
                .code(status.value())
                .messages(messages)
                .data(data)
                .build();
    }


}
