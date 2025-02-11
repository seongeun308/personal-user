package com.personal.user.core.service;

import com.personal.user.dto.SignUpRequest;

public interface UserAccountService {
    void signUp(SignUpRequest request);
}