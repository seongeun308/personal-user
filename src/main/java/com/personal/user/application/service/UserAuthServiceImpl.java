package com.personal.user.application.service;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.dto.TokenPair;
import com.personal.user.application.dto.request.LoginRequest;
import com.personal.user.application.model.UserClaims;
import com.personal.user.core.domain.Role;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.core.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAccountService userAccountService;
    private final TokenService tokenService;

    @Override
    public TokenPair login(LoginRequest loginRequest) {
        User user = authenticate(loginRequest);

        Map<String, Object> claims = Map.of(
                UserClaims.ROLE.getClaimName(), user.getRole()
        );

        return tokenService.issueToken(user.getUserId(), claims);
    }

    @Override
    public void logout(String accessToken) {
        tokenService.validateAccessToken(accessToken);
        tokenService.revokeToken(accessToken);
    }

    @Override
    public TokenPair reissue(String refreshToken) {
        tokenService.validateRefreshToken(refreshToken);

        Long userId = tokenService.getUserIdFromRefreshToken(refreshToken);
        User user = userAccountService.getUserByUserId(userId);

        Map<String, Object> claims = Map.of(
                UserClaims.ROLE.getClaimName(), user.getRole()
        );

        return tokenService.reissueToken(refreshToken, claims);
    }

    @Override
    public void unregister(String accessToken) {
        Long userId = tokenService.getUserIdFromAccessToken(accessToken);

        tokenService.revokeToken(accessToken);
        userAccountService.deleteUser(userId);
    }

    private User authenticate(LoginRequest loginRequest) {
        User user = userAccountService.getUserByEmail(loginRequest.getEmail());
        boolean isPasswordMatched = BCrypt.checkpw(loginRequest.getPassword(), user.getPassword());

        if (!isPasswordMatched)
            throw new UserAuthException(UserErrorCode.AUTHENTICATION_FAILED);

        return user;
    }
}