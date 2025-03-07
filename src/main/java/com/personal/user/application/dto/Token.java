package com.personal.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
@AllArgsConstructor
public class Token {
    private String token;
    private ZonedDateTime expires;
}
