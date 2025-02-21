package com.personal.user.application.service;

import com.personal.user.application.common.api.StatusCode;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.common.util.JwtTokenUtil;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.dto.request.SignUpRequest;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.core.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserAuthServiceImplTest {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void 로그인_성공() {
        SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "test1122!");
        Long userId = userAccountService.signUp(signUpRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122!");
        String token = userAuthService.login(loginRequest);

        assertThat(userId).isEqualTo(jwtTokenUtil.getUserIdFromToken(token));
    }

    @Test
    void 로그인_유저_인증_실패() {
        SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "test1122!");
        userAccountService.signUp(signUpRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1122");

        UserAuthException e = assertThrows(UserAuthException.class, () -> userAuthService.login(loginRequest));

        assertThat(e.getStatusCode()).isEqualTo(StatusCode.USER_NOT_FOUND);
    }
}