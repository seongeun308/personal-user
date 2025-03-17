package com.personal.user.application.common.aop;

import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.application.service.TokenService;
import com.personal.user.core.domain.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@RequiredArgsConstructor
public class AuthorizeAspect {

    private static final String HEADER_STRING = "Authorization";
    private static final String PREFIX = "Bearer ";
    private final TokenService tokenService;

    @Around("@annotation(com.personal.user.application.common.annotation.AdminOnly)")
    public Object checkAdminRole(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null)
            throw new IllegalStateException("Request 정보를 가져올 수 없습니다.");

        HttpServletRequest request = attributes.getRequest();
        String accessToken = extractAccessToken(request);
        String roleName = tokenService.getRoleFromAccessToken(accessToken);

        if (roleName != null && Role.valueOf(roleName).equals(Role.ADMIN))
            return joinPoint.proceed();

        throw new UserAuthException(UserErrorCode.ACCESS_DENIED);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(PREFIX))
            return header.substring(PREFIX.length());
        return null;
    }
}