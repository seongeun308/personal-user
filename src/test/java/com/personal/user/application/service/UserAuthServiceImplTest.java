package com.personal.user.application.service;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.request.RegisterRequest;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.core.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserAuthServiceImplTest {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserAccountService userAccountService;
    @MockitoBean
    private TokenService tokenService;

    @Test
    void 로그인_성공() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test1122!");
        Long userId = userAccountService.addUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122!");

        TokenDto mockAccessToken = TokenDto.builder().token("testAT").expiresAt("testATExp").build();
        TokenDto mockRefreshToken = TokenDto.builder().token("testRT").expiresAt("testRTExp").build();
        when(tokenService.issueToken(userId))
                .thenReturn(new TokenPair(mockAccessToken, mockRefreshToken));

        TokenPair tokenPair = userAuthService.login(loginRequest);

        assertThat(tokenPair.getAccessToken()).isNotNull();
        assertThat(tokenPair.getRefreshToken()).isNotNull();
    }

    @Test
    void 로그인_유저_인증_실패() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test1122!");
        userAccountService.addUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122");

        UserAuthException e = assertThrows(UserAuthException.class, () -> userAuthService.login(loginRequest));

        assertThat(e.getErrorCode()).isEqualTo(UserErrorCode.AUTHENTICATION_FAILED);
    }
}