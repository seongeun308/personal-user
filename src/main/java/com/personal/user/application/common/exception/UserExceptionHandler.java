package com.personal.user.application.common.exception;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAccountException;
import com.personal.user.application.common.exception.user.UserAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserAccountException.class)
    public ResponseEntity<Api<Void>> handleUserAccountException(UserAccountException e) {
        UserErrorCode statusCode = e.getErrorCode();

        Api<Void> api = Api.error(statusCode);

        log.error("{}", api);

        return ResponseEntity.status(statusCode.getCode())
                .body(api);
    }

    @ExceptionHandler(value = UserAuthException.class)
    public ResponseEntity<Api<Void>> handleUserAuthException(UserAuthException e) {
        UserErrorCode statusCode = e.getErrorCode();

        Api<Void> api = Api.error(statusCode);

        log.error("{}", api);

        return ResponseEntity.status(statusCode.getCode())
                .body(api);
    }

}
