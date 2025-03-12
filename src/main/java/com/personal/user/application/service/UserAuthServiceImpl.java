package com.personal.user.application.service;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.dto.TokenDto;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.repository.UserRepository;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    @Override
    public Map<String, TokenDto> login(LoginRequest loginRequest) {
        User user = authenticate(loginRequest);

        TokenDto accessToken = jwtTokenService.generateAccessToken(user.getUserId());
        TokenDto refreshToken = jwtTokenService.generateRefreshToken(user.getUserId());

        return Map.of("accessToken", accessToken,
                "refreshToken", refreshToken);
    }

    private User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserAuthException(UserErrorCode.USER_NOT_FOUND));

        boolean isPasswordMatched = BCrypt.checkpw(loginRequest.getPassword(), user.getPassword());

        if (!isPasswordMatched)
            throw new UserAuthException(UserErrorCode.USER_NOT_FOUND);

        return user;
    }
}