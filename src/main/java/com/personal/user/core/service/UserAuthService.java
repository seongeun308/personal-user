package com.personal.user.core.service;

import com.personal.user.application.dto.Token;
import com.personal.user.application.dto.request.LoginRequest;

import java.util.Map;

public interface UserAuthService {
    Map<String, Token> login(LoginRequest loginRequest);
}
