package com.personal.user.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private Long userId;
}
