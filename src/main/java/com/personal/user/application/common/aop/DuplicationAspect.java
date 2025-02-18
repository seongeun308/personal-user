package com.personal.user.application.common.aop;

import com.personal.user.application.common.api.StatusCode;
import com.personal.user.application.common.exception.user.UserAccountException;
import com.personal.user.application.dto.request.SignUpRequest;
import com.personal.user.application.repository.UserRepository;
import com.personal.user.core.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DuplicationAspect {

    private final UserAccountService userAccountService;

    @Around("@annotation(com.personal.user.application.common.annotation.Duplication)")
    public Object beforeDuplication(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof String email) {
                checkEmailDuplication(email);
                break;
            }
            if (arg instanceof SignUpRequest request) {
                checkEmailDuplication(request.getEmail());
                break;
            }
        }
        return joinPoint.proceed();
    }

    private void checkEmailDuplication(String email) {
        userAccountService.duplicateEmail(email);
    }

}
