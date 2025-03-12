package com.personal.user.application.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    SUCCESS("성공"),
    FAIL("실패")
    ;

    private final String status;
}
