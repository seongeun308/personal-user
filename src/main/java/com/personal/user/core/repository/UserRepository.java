package com.personal.user.core.repository;

import com.personal.user.core.domain.User;

public interface UserRepository {
    User save(User user);
}
