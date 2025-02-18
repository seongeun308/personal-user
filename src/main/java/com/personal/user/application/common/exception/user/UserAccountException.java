package com.personal.user.application.common.exception.user;

import com.personal.user.application.common.api.StatusCode;
import lombok.Getter;

@Getter
public class UserAccountException extends RuntimeException {
    private final StatusCode statusCode;

    public UserAccountException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }
}
