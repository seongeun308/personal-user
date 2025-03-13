package com.personal.user.application.service;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.request.SignUpRequest;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.core.service.UserAuthService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
        SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "test1122!");
        Long userId = userAccountService.signUp(signUpRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122!");

        TokenDto mockAccessToken = new TokenDto("testAT", "testATExp");
        TokenDto mockRefreshToken = new TokenDto("testRT", "testRTExp");
        when(tokenService.issueToken(userId))
                .thenReturn(new TokenPair(mockAccessToken, mockRefreshToken));

        TokenPair tokenPair = userAuthService.login(loginRequest);

        assertThat(tokenPair.getAccessToken()).isNotNull();
        assertThat(tokenPair.getRefreshToken()).isNotNull();
    }

    @Test
    void 로그인_유저_인증_실패() {
        SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "test1122!");
        userAccountService.signUp(signUpRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122");

        UserAuthException e = assertThrows(UserAuthException.class, () -> userAuthService.login(loginRequest));

        assertThat(e.getErrorCode()).isEqualTo(UserErrorCode.AUTHENTICATION_FAILED);
    }
}