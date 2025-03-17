package com.personal.user.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserClaims {
    ROLE("role")
    ;

    private final String claimName;
}