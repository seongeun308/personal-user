package com.personal.user.application.service;

import com.personal.user.application.common.converter.UserConverter;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.application.dto.request.SignUpRequest;
import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.common.exception.user.UserAccountException;
import com.personal.user.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;

    @Override
    public Long signUp(SignUpRequest request) {
        String encodedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User user = userRepository.save(UserConverter.toUser(request, encodedPassword));

        log.info("Sign up successful {}", user);

        return user.getUserId();
    }

    @Override
    public void duplicateEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new UserAccountException(UserErrorCode.EMAIL_CONFLICT);
    }
}