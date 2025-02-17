package com.personal.user.service;

import com.personal.user.core.service.UserAccountService;
import com.personal.user.dto.SignUpRequest;
import com.personal.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserAccountServiceImplTest {

    @Autowired
    private UserAccountService userAccountService;

    @Test
    void signUp() {
        SignUpRequest signUpRequest = new SignUpRequest("test@test.com", "test");
        userAccountService.signUp(signUpRequest);
    }
}