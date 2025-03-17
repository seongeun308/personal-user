package com.personal.user.application.dto.response;

import com.personal.user.core.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ViewResponse {
    private String email;
    private Role role;
}
