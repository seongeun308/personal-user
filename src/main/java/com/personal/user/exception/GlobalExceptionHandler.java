package com.personal.user.exception;

import com.personal.user.api.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api<Void>> handleUserException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Api<Void> api = Api.error(HttpStatus.BAD_REQUEST, messages, null);

        log.error("{}", api);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(api);
    }
}
