package com.personal.user.application.common.converter;

import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.model.RefreshToken;

public class TokenConverter {

    public static RefreshToken toRefreshToken(Long userId, TokenDto tokenDto) {
        return RefreshToken.builder()
                .userId(userId)
                .token(tokenDto.getToken())
                .createdAt(tokenDto.getCreatedAt())
                .expireAt(tokenDto.getExpiresAt()).build();
    }
}
