package com.personal.user.application.common.exception.user;

import com.personal.user.application.common.api.code.UserErrorCode;
import lombok.Getter;

@Getter
public class UserAuthException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserAuthException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}