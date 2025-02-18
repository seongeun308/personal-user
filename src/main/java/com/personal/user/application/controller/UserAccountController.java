package com.personal.user.application.controller;

import com.personal.user.application.common.annotation.Duplication;
import com.personal.user.application.common.api.Api;
import com.personal.user.application.common.api.StatusCode;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.application.dto.request.SignUpRequest;
import com.personal.user.application.dto.response.SignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Duplication
    @PostMapping
    public Api<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        Long userId = userAccountService.signUp(signUpRequest);

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .userId(userId)
                .token("")
                .build();

        return Api.ok(StatusCode.CREATED, signUpResponse);
    }

    @PostMapping("/duple-email")
    public Api<String> duplicateEmail(@RequestBody String email) {
        userAccountService.duplicateEmail(email);
        return Api.ok(StatusCode.OK, email);
    }
}