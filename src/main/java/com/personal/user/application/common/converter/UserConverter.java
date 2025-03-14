package com.personal.user.application.common.converter;

import com.personal.user.core.domain.User;
import com.personal.user.application.dto.request.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public static User toUser(RegisterRequest registerRequest, String encodedPassword) {
        return User.builder()
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .build();
    }
}
