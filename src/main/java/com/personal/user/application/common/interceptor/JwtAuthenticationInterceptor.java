package com.personal.user.application.common.interceptor;

import com.personal.user.application.common.api.code.TokenErrorCode;
import com.personal.user.application.common.exception.token.TokenException;
import com.personal.user.application.repository.UserRepository;
import com.personal.user.application.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private static final String HEADER_STRING = "Authorization";
    private static final String PREFIX = "Bearer ";
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = resolveToken(request);

        if (accessToken == null)
            throw new TokenException(TokenErrorCode.TOKEN_DOES_NOT_EXIST);

        jwtTokenService.validateToken(accessToken);

        Long userId = jwtTokenService.getUserIdFromToken(accessToken);
        if (!userRepository.existsById(userId))
            throw new TokenException(TokenErrorCode.INVALID_TOKEN_VALUE);

        return true;
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(PREFIX))
            return header.substring(PREFIX.length());
        return null;
    }
}