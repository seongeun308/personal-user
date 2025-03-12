package com.personal.user.application.common.exception.token;

import com.personal.user.application.common.api.code.TokenErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {
    private final TokenErrorCode errorCode;

    public TokenException(TokenErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
