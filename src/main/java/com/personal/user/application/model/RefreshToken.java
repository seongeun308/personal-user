package com.personal.user.application.model;

import jakarta.persistence.Id;
import lombok.*;

@Getter
@ToString
@Builder
public class RefreshToken {
    @Id
    private Long userId;
    private String token;
    private String createdAt;
    private String expireAt;
}
