package com.personal.user.application.controller;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.response.TokenResponse;
import com.personal.user.core.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<Api<TokenResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        Map<String, TokenDto> tokens = userAuthService.login(loginRequest);
        TokenDto accessToken = tokens.get("accessToken");
        TokenDto refreshToken = tokens.get("refreshToken");

        ResponseCookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);
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
