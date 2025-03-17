package com.personal.user.application.common.converter;

import com.personal.user.core.domain.Role;
import com.personal.user.core.domain.User;
import com.personal.user.application.dto.request.RegisterRequest;

public class UserConverter {

    public static User toUser(RegisterRequest registerRequest, String encodedPassword) {
        return User.builder()
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .build();
    }

    public static User toUser(RegisterRequest registerRequest, String encodedPassword, Role role) {
        return User.builder()
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .role(role)
                .build();
    }

    public static User toUser(RegisterRequest registerRequest, String encodedPassword, Long userId) {
        return User.builder()
                .userId(userId)
                .email(registerRequest.getEmail())
                .password(encodedPassword)
                .build();
    }
}
