package com.personal.user.service;

import com.personal.user.converter.UserConverter;
import com.personal.user.core.domain.User;
import com.personal.user.core.service.UserAccountService;
import com.personal.user.dto.SignUpRequest;
import com.personal.user.repository.UserRepository;
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
    public void signUp(SignUpRequest request) {
        String encodedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User user = UserConverter.toUser(request, encodedPassword);
        userRepository.save(user);

        log.info("Sign up successful {}", user);
    }
}