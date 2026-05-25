package com.app.questing.controller;

import com.app.questing.config.JwtProvider;
import com.app.questing.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void createTodoValidationFail() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(1L);

        String requestBody = """
                {
                 "timeSlot": "",
                 "title": "",
                 "category": "",
                 "durationTime": 0
                }
                """;

        mockMvc.perform(post("/api/todos")
                        .header("Authorization", "Bearer valid-token")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("요청값이 올바르지 않습니다."))
                .andExpect(jsonPath("$.fieldErrors.timeSlot").exists())
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.category").exists())
                .andExpect(jsonPath("$.fieldErrors.durationTime").exists());
    }

    @Test
    void updateTodoValidationFail() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(1L);

        String requestBody = """
                {
                 "timeSlot": "",
                 "title": "",
                 "category": "",
                 "durationTime": 0
                }
                """;

        mockMvc.perform(put("/api/todos/1")
                        .header("Authorization", "Bearer valid-token")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.timeSlot").exists())
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.category").exists())
                .andExpect(jsonPath("$.fieldErrors.durationTime").exists());
    }
}
