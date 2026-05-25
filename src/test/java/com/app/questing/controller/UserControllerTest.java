package com.app.questing.controller;


import com.app.questing.config.JwtProvider;
import com.app.questing.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("회원가입 요청값이 올바르지 않으면 400과 fieldErrors를 응답한다.")
    void signupValidationFail() throws Exception {
        String requestBody = """
                {
                  "loginId": "",
                  "password": "1234",
                  "userName": "",
                  "nickName": "a",
                  "email": "invalid-email",
                  "birth": "2030-01-01"
                }
                """;

        mockMvc.perform(post("/api/users/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("요청값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.fieldErrors.loginId").exists())
                .andExpect(jsonPath("$.fieldErrors.password").exists())
                .andExpect(jsonPath("$.fieldErrors.userName").exists())
                .andExpect(jsonPath("$.fieldErrors.nickName").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.birth").exists());

    }

    @Test
    @DisplayName("로그인 요청값이 올바르지 않으면 400과 fieldErrors를 응답한다.")
    void loginValidationFail() throws Exception {
        String requestBody = """
                {
                "loginId": "",
                "password": ""
                }
                """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.loginId").exists())
                .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

}
