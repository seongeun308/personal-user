package com.personal.user.application.controller;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.response.TokenResponse;
import com.personal.user.application.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtTokenService jwtTokenService;

    @PostMapping("/auth/refresh")
    public ResponseEntity<Api<TokenResponse>> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            throw new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST);

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow(() -> new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST))
                .getValue();

        Long userId = jwtTokenService.getUserIdFromToken(refreshToken);
        jwtTokenService.checkRefreshToken(userId, refreshToken);

        TokenDto accessToken = jwtTokenService.generateAccessToken(userId);
        TokenDto newRefreshToken = jwtTokenService.generateRefreshToken(userId);

        ResponseCookie refreshTokenCookie = getRefreshTokenCookie(newRefreshToken);
        TokenResponse tokenResponse = new TokenResponse(accessToken.getExpiredAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken.getToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Api.ok(tokenResponse));
    }

    private static ResponseCookie getRefreshTokenCookie(TokenDto refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
