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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenHelper jwtTokenHelper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenPair issueToken(Long userId) {
        TokenDto accessToken = jwtTokenHelper.issueAccessToken(userId);
        TokenDto refreshToken = jwtTokenHelper.issueRefreshToken();

        refreshTokenRepository.save(new RefreshToken(refreshToken.getToken(), userId));

        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public TokenPair reissueToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST));

        refreshTokenRepository.deleteByToken(refreshToken);

        return issueToken(token.getUserId());
    }

    @Override
    public Long getUserIdFromAccessToken(String accessToken) {
        String userId = jwtTokenHelper.getAccessTokenClaims(accessToken)
                .getPayload()
                .getSubject();

        return Long.valueOf(userId);
    }

    @Override
    public void revokeToken(Long userId) {

    }

    @Override
    public void validateAccessToken(String accessToken) {
        try {
            jwtTokenHelper.getAccessTokenClaims(accessToken);
        } catch (ExpiredJwtException ignored) {
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
        }
        catch (JwtException ignored) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_FORMAT);
        }
    }

    @Override
    public void validateRefreshToken(String refreshToken) {
        try {
            jwtTokenHelper.getRefreshTokenClaims(refreshToken);
        } catch (ExpiredJwtException ignored) {
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
        }
        catch (JwtException ignored) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_FORMAT);
        }
    }
}
