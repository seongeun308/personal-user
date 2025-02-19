package com.personal.user.application.common.exception.user;

import com.personal.user.application.common.api.StatusCode;
import lombok.Getter;

@Getter
public class UserAuthException extends RuntimeException {
    private final StatusCode statusCode;

    public UserAuthException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}