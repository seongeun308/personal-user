package com.personal.user.application.controller;

import com.personal.user.application.common.annotation.AdminOnly;
import com.personal.user.application.common.api.Api;
import com.personal.user.application.dto.request.RegisterRequest;
import com.personal.user.application.dto.response.RegisterResponse;
import com.personal.user.application.dto.response.ViewResponse;
import com.personal.user.core.domain.Role;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AdminOnly
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserAccountService userAccountService;

    @PostMapping
    public Api<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        Long userId = userAccountService.addUser(registerRequest, Role.ADMIN);
        RegisterResponse registerResponse = RegisterResponse.builder()
                .userId(userId)
                .build();

        return Api.ok(registerResponse);
    }

    @GetMapping("/{userId}")
    public Api<ViewResponse> viewUser(@PathVariable Long userId) {
        User user = userAccountService.getUserByUserId(userId);
        return Api.ok(new ViewResponse(user.getEmail(), user.getRole()));
    }

    @PutMapping("/{userId}")
    public Api<Void> updateUser(@PathVariable Long userId, @RequestBody RegisterRequest registerRequest) {
        userAccountService.updateUser(registerRequest, userId);
        return Api.ok(null);
    }

    @DeleteMapping("/{userId}")
    public Api<Void> deleteUser(@PathVariable Long userId) {
        userAccountService.deleteUser(userId);
        return Api.ok(null);
    }
}
