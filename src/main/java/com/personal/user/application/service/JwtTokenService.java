package com.personal.user.application.service;

import com.personal.user.application.dto.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtTokenService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Token generateAccessToken(Long userId) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusMinutes(15L);

        String accessToken = createTokenByBuilder(userId, toDate(now), toDate(expiration));

        return new Token(accessToken, expiration);
    }

    public Token generateRefreshToken(Long userId) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusDays(7L);

        String refreshToken = createTokenByBuilder(userId, toDate(now), toDate(expiration));

        return new Token(refreshToken, expiration);
    }

    private String createTokenByBuilder(Long userId, Date now, Date expiration) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public Long getUserIdFromToken(String token) {
        String userId = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.valueOf(userId);
    }

    public boolean validateToken(String token, Long userId) {
        Long extractedUserId = getUserIdFromToken(token);
        return Objects.equals(extractedUserId, userId) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}
