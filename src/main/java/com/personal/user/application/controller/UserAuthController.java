package com.personal.user.application.controller;

import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.StatusCode;
import com.personal.user.application.dto.Token;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.response.LoginResponse;
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
    public ResponseEntity<Api<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        Map<String, Token> tokens = userAuthService.login(loginRequest);
        Token accessToken = tokens.get("accessToken");
        Token refreshToken = tokens.get("refreshToken");

        ResponseCookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);

        LoginResponse loginResponse = new LoginResponse(
                accessToken.getExpires().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                refreshToken.getExpires().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken.getToken())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Api.ok(StatusCode.OK, loginResponse));
    }

    private static ResponseCookie getRefreshTokenCookie(Token refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
