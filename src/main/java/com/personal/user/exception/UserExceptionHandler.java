package com.personal.user.exception;

import com.personal.user.api.Api;
import com.personal.user.api.StatusCode;
import com.personal.user.exception.user.UserAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserAccountException.class)
    public ResponseEntity<Api<Void>> handleUserException(UserAccountException e) {
        StatusCode statusCode = e.getStatusCode();

        return ResponseEntity.status(statusCode.getCode())
                .body(Api.error(statusCode, null));
    }

}
