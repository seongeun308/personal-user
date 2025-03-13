package com.personal.user.application.service;

import com.personal.user.application.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

    @Value("${jwt.secret-key}")
    private String secretKey;
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 15;
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;

    public TokenDto issueAccessToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusSeconds(ACCESS_TOKEN_VALIDITY_SECONDS);

        String accessToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .subject(String.valueOf(userId))
                .issuedAt(toDate(now))
                .expiration(toDate(expiration))
                .compact();

        return new TokenDto(accessToken, expiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public TokenDto issueRefreshToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusSeconds(REFRESH_TOKEN_VALIDITY_SECONDS);

        // TODO : RS256 알고리즘 적용
        String refreshToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .subject(String.valueOf(userId))
                .issuedAt(toDate(now))
                .expiration(toDate(expiration))
                .compact();

        return new TokenDto(refreshToken, expiration.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public Jws<Claims> getAccessTokenClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(accessToken);
    }

    public Jws<Claims> getRefreshTokenClaims(String refreshToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(refreshToken);
    }

    private Date toDate(LocalDateTime expiration) {
        return Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
    }
}
