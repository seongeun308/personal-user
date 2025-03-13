package com.personal.user.application.common.exception;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity<Api<Void>> handleTokenException(final TokenException e) {
        TokenErrorCode errorCode = e.getErrorCode();

        Api<Void> api = Api.error(errorCode);

        log.error("{}", api);

        return ResponseEntity.status(errorCode.getCode())
                .body(api);
    }
}
