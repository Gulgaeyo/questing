package com.app.questing.controller;

import com.app.questing.config.JwtProvider;
import com.app.questing.dto.todo.TodoResponse;
import com.app.questing.service.TodoService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void getTodoWithoutTokenFail() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("인증 토큰이 필요합니다."));
    }

    @Test
    void getTodoWithInvalidAuthorizationFormatFail() throws Exception {
        mockMvc.perform(get("/api/todos")
                        .header("Authorization", "invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("인증 토큰이 필요합니다."));
    }

    @Test
    void getTodoWithInvalidTokenFail() throws Exception {
        willThrow(new JWTVerificationException("invalid token"))
                .given(jwtProvider)
                .getUserId("invalid-token");

        mockMvc.perform(get("/api/todos")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));

    }

    @Test
    void getTodoWithAuthenticatedUserId() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(2L);

        TodoResponse todo = new TodoResponse();
        todo.setId(1L);
        todo.setUserId(2L);
        todo.setTitle("코딩 문제 풀기");
        todo.setCategory("INTELLIGENCE");
        todo.setTimeSlot("MORNING");
        todo.setDurationTime(30);
        todo.setIsCompleted(false);

        given(todoService.getTodayTodos(2L)).willReturn(List.of(todo));

        mockMvc.perform(get("/api/todos")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].title").value("코딩 문제 풀기"));

        then(todoService)
                .should(times(1))
                .getTodayTodos(2L);
    }
}
