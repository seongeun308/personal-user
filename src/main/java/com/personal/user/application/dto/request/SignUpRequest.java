package com.personal.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SignUpRequest {
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}