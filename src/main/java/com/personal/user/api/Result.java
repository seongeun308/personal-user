package com.personal.user.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    SUCCESS("성공"),
    FAIL("실패")
    ;

    private String status;
}
