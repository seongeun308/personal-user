package com.personal.user.application.service;

import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.core.domain.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final RedisService redisService;

    public TokenDto generateAccessToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMinutes(15L);

        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .subject(String.valueOf(userId))
                .issuedAt(toDate(now))
                .expiration(toDate(expiration))
                .compact();

        return new TokenDto(accessToken, expiration);
    }

    public TokenDto generateRefreshToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusDays(7L);

        // TODO : RS256 알고리즘 적용
        String refreshToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .subject(String.valueOf(userId))
                .issuedAt(toDate(now))
                .expiration(toDate(expiration))
                .compact();

        Token token = new Token(userId, refreshToken, expiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        saveRefreshToken(token);

        return new TokenDto(refreshToken, expiration);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Long getUserIdFromToken(String token) {
        String userId = getClaimsFromToken(token)
                .getPayload()
                .getSubject();

        return Long.valueOf(userId);
    }

    public void validateToken(String token) {
        Date expiration = getClaimsFromToken(token)
                .getPayload()
                .getExpiration();

        if (expiration.before(new Date()))
            throw new TokenException(TokenErrorCode.EXPIRED_TOKEN);
    }

    private void saveRefreshToken(Token token) {
        redisService.saveData(token.getUserId(), token);
    }

    public void checkRefreshToken(Long userId, String refreshToken) {
        Token token = redisService.getData(userId);

        if (!token.getToken().equals(refreshToken))
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_VALUE);
    }

    private Jws<Claims> getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_FORMAT);
        }
    }
}
