package com.personal.user.application.common.exception.user;

import com.personal.user.application.common.api.code.ErrorCode;
import lombok.Getter;

@Getter
public class AccountDeleteException extends RuntimeException {
    private final ErrorCode errorCode;

    public AccountDeleteException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}