package com.personal.user.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class TokenDto {
    private String token;
    private String expiresAt;
    private String createdAt;
}
