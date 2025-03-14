package com.personal.user.application.service;

import com.personal.user.application.common.converter.UserConverter;
import com.personal.user.application.common.exception.user.AccountDeleteException;
import com.personal.user.application.common.exception.user.UserAuthException;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.application.dto.request.RegisterRequest;
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
    public Long addUser(RegisterRequest request) {
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

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserAuthException(UserErrorCode.AUTHENTICATION_FAILED));
    }

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserAuthException(UserErrorCode.AUTHENTICATION_FAILED));
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new AccountDeleteException(UserErrorCode.UNEXPECT_ERROR);
        }

        log.info("Deleted user account :{}", userId);
    }
}