package com.personal.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.user.application.common.api.Result;
import com.personal.user.application.common.api.code.UserErrorCode;
import com.personal.user.application.dto.request.RegisterRequest;
import com.personal.user.core.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입_성공하면_200() throws Exception {
        String content = objectMapper.writeValueAsString(new RegisterRequest("test@test.com", "test1122!"));

        mockMvc.perform(post("/register")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(Result.SUCCESS.getStatus()))
                .andExpect(jsonPath("$.data.userId").value(1L));
    }

    @Test
    void 회원가입_이메일_중복_시_409() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test1122!");
        userAccountService.addUser(registerRequest);

        String content = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/register")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(Result.FAIL.getStatus()))
                .andExpect(jsonPath("$.code").value(UserErrorCode.EMAIL_CONFLICT.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.EMAIL_CONFLICT.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    void 회원가입_올바르지_않은_형식으로_실패_시_400() throws Exception {
        RegisterRequest wrongRegisterRequest = new RegisterRequest("testtest.com", "test");
        String wrongContent = objectMapper.writeValueAsString(wrongRegisterRequest);

        mockMvc.perform(post("/register")
                        .content(wrongContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(Result.FAIL.getStatus()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", containsString("올바르지 않은 이메일 형식입니다.")))
                .andExpect(jsonPath("$.message", containsString("비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void 회원가입_필수_값_누락_시_400() throws Exception {
        RegisterRequest nullRegisterRequest = new RegisterRequest(null, null);
        String nullContent = objectMapper.writeValueAsString(nullRegisterRequest);

        mockMvc.perform(post("/register")
                        .content(nullContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(Result.FAIL.getStatus()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", containsString("이메일은 필수 입력값입니다.")))
                .andExpect(jsonPath("$.message", containsString("비밀번호는 필수 입력값입니다.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void 이메일_중복_시_409() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test1122!");
        userAccountService.addUser(registerRequest);

        mockMvc.perform(post("/duple-email")
                        .content(registerRequest.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(Result.FAIL.getStatus()))
                .andExpect(jsonPath("$.code").value(UserErrorCode.EMAIL_CONFLICT.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.EMAIL_CONFLICT.getMessage()))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}