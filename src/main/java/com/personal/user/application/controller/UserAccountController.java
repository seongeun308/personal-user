package com.personal.user.application.controller;

import com.personal.user.application.common.annotation.Duplication;
import com.personal.user.application.common.api.Api;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.application.dto.request.RegisterRequest;
import com.personal.user.application.dto.response.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Duplication
    @PostMapping("/register")
    public Api<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        Long userId = userAccountService.addUser(registerRequest);

        RegisterResponse registerResponse = RegisterResponse.builder()
                .userId(userId)
                .build();

        return Api.ok(registerResponse);
    }

    @Duplication
    @PostMapping("/duple-email")
    public Api<String> duplicateEmail(@RequestBody String email) {
        return Api.ok(email);
    }
}