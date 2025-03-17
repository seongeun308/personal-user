package com.personal.user.core.service;

import com.personal.user.application.dto.request.RegisterRequest;
import com.personal.user.core.domain.Role;
import com.personal.user.core.domain.User;

public interface UserAccountService {
    Long addUser(RegisterRequest request);

    Long addUser(RegisterRequest request, Role role);

    void updateUser(RegisterRequest request, Long userId);

    void duplicateEmail(String email);

    User getUserByEmail(String userId);

    User getUserByUserId(Long userId);

    void deleteUser(Long userId);
}