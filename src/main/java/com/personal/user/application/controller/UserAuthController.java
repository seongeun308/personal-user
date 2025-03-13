package com.personal.user.application.controller;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.response.TokenResponse;
import com.personal.user.application.service.TokenService;
import com.personal.user.core.service.UserAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String HEADER_STRING = "Authorization";
    private static final String PREFIX = "Bearer ";
    private final UserAuthService userAuthService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Api<TokenResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenPair tokenPair = userAuthService.login(loginRequest);
        return assembleTokenResponse(tokenPair);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Api<TokenResponse>> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            throw new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST);
        
        String refreshToken = getTokenFromCookie(cookies);
        TokenPair tokenPair = tokenService.reissueToken(refreshToken);

        return assembleTokenResponse(tokenPair);
    }

    @PostMapping("/logout")
    public Api<Void> logout(HttpServletRequest request) {
        String accessToken = extractAccessToken(request);
        try {
            tokenService.revokeToken(accessToken);
        } catch (TokenException | IllegalArgumentException ignored) {}

        return Api.ok(null);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(PREFIX))
            return header.substring(PREFIX.length());
        return null;
    }

    private ResponseEntity<Api<TokenResponse>> assembleTokenResponse(TokenPair tokenPair) {
        ResponseCookie refreshTokenCookie = createCookieWithToken(tokenPair.getRefreshToken());
        TokenResponse tokenResponse = new TokenResponse(tokenPair.getAccessToken().getExpiresAt());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenPair.getAccessToken().getToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Api.ok(tokenResponse));
    }

    private String getTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST))
                .getValue();
    }

    private ResponseCookie createCookieWithToken(TokenDto refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/reissue")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
