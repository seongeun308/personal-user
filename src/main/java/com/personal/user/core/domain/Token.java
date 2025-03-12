package com.personal.user.core.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    private Long userId;
    private String token;
    private String expiredAt;
    private String createdAt;
}
