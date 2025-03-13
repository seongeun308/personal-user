package com.personal.user.application.service;

import com.personal.user.application.dto.TokenPair;

public interface TokenService {
    TokenPair issueToken(Long userId);

    TokenPair reissueToken(String refreshToken);

    Long getUserIdFromAccessToken(String accessToken);

    void revokeToken(Long userId);

    void validateAccessToken(String accessToken);

    void validateRefreshToken(String refreshToken);
}
