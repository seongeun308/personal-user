package com.personal.user.core.service;

import com.personal.user.dto.request.SignUpRequest;

public interface UserAccountService {
    Long signUp(SignUpRequest request);

    void duplicateEmail(String email);
}