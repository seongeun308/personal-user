package com.personal.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TokenPair {
    private final TokenDto accessToken;
    private final TokenDto refreshToken;
}
