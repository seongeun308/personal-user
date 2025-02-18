package com.personal.user.application.common.converter;

import com.personal.user.core.domain.User;
import com.personal.user.application.dto.request.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public static User toUser(SignUpRequest signUpRequest, String encodedPassword) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .build();
    }
}
