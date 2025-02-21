package com.personal.user.core.service;

import com.personal.user.application.dto.Token;
import com.personal.user.application.dto.request.LoginRequest;

public interface UserAuthService {
    Token login(LoginRequest loginRequest);
}
