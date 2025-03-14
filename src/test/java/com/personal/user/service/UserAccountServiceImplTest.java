package com.personal.user.service;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAccountException;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.application.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class UserAccountServiceImplTest {

    @Autowired
    private UserAccountService userAccountService;

    @Test
    void signUp() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test");
        userAccountService.addUser(registerRequest);
    }

    @Test
    void duplicateEmail() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test");
        userAccountService.addUser(registerRequest);

        UserAccountException e = assertThrows(UserAccountException.class, () ->
                userAccountService.duplicateEmail("test@test.com"));

        assertThat(e.getErrorCode()).isEqualTo(UserErrorCode.EMAIL_CONFLICT);
    }
}