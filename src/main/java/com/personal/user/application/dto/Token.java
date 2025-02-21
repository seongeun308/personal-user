package com.personal.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
}
