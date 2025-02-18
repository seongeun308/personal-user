package com.personal.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private Long userId;
    private String token;
}
