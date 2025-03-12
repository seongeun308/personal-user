package com.personal.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class TokenDto {
    private String token;
    private LocalDateTime expiredAt;
}
