package com.personal.user.application.service;

import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.model.RefreshToken;
import com.personal.user.application.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenHelper jwtTokenHelper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public TokenPair issueToken(Long userId) {
        TokenDto accessToken = jwtTokenHelper.issueAccessToken(userId);
        TokenDto refreshToken = jwtTokenHelper.issueRefreshToken(userId);

        refreshTokenRepository.save(new RefreshToken(userId, refreshToken.getToken()));

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair reissueToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        Long userId = getUserIdFromRefreshToken(refreshToken);
        return issueToken(userId);
    }

    public Long getUserIdFromAccessToken(String accessToken) {
        String userId = jwtTokenHelper.getAccessTokenClaims(accessToken)
                .getPayload()
                .getSubject();

        return Long.valueOf(userId);
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        String userId = jwtTokenHelper.getRefreshTokenClaims(refreshToken)
                .getPayload()
                .getSubject();

        return Long.valueOf(userId);
    }

    public Date getExpirationFromAccessToken(String accessToken) {
        return jwtTokenHelper.getAccessTokenClaims(accessToken)
                .getPayload()
                .getExpiration();
    }

    public void revokeToken(String accessToken) {
        Date expiration = getExpirationFromAccessToken(accessToken);
        tokenBlacklistService.addToBlacklist(accessToken, toSeconds(expiration.toInstant()));

        Long userId = getUserIdFromAccessToken(accessToken);
        refreshTokenRepository.deleteById(userId);
    }

    public void validateAccessToken(String accessToken) {
        try {
            jwtTokenHelper.getAccessTokenClaims(accessToken);
        } catch (ExpiredJwtException ignored) {
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
        }
        catch (JwtException ignored) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_FORMAT);
        }

        if (tokenBlacklistService.isBlacklisted(accessToken))
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            jwtTokenHelper.getRefreshTokenClaims(refreshToken);

            Long userId = getUserIdFromRefreshToken(refreshToken);
            if (!refreshTokenRepository.existsById(userId))
                throw new TokenException(TokenErrorCode.TOKEN_NOT_FOUND);
        } catch (ExpiredJwtException ignored) {
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
        }
        catch (JwtException ignored) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_FORMAT);
        }
    }

    private long toSeconds(Temporal expiration) {
        return Duration.between(LocalDateTime.now(), expiration).getSeconds();
    }
}
