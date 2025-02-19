package com.personal.user.core.service;

import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.core.domain.User;

public interface UserAuthService {
    User authenticate(LoginRequest loginRequest);
}
