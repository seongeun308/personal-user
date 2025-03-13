package com.personal.user.core.service;

import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.dto.request.LoginRequest;

public interface UserAuthService {
    TokenPair login(LoginRequest loginRequest);
}
