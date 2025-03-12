package com.personal.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TokenResponse {
    private String expiredAt;   // accessToken
}
