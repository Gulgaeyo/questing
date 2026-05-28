package com.app.questing.controller;

import com.app.questing.config.JwtProvider;
import com.app.questing.dto.habit.HabitDTO;
import com.app.questing.service.HabitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(HabitController.class)
public class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HabitService habitService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void createHabitValidationFail() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(1L);

        String requestBody = """
                {
                "title": "",
                "content": "",
                "category": "",
                "durationTime": 0,
                "endDate": "2026-05-24"
                }
                """;

        mockMvc.perform(post("/api/habits")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.content").exists())
                .andExpect(jsonPath("$.fieldErrors.category").exists())
                .andExpect(jsonPath("$.fieldErrors.durationTime").exists())
                .andExpect(jsonPath("$.fieldErrors.strtDate").exists())
                .andExpect(jsonPath("$.fieldErrors.endDate").exists());

    }

    @Test
    void updateHabitValidationFail() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(1L);

        String requestBody = """
                {
                  "title": "",
                  "content": "",
                  "category": "",
                  "durationTime": 0,
                  "endDate": "2026-05-24"
                }
                """;

        mockMvc.perform(put("/api/habits/1")
                        .header("Authorization", "Bearer valid-token")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.content").exists())
                .andExpect(jsonPath("$.fieldErrors.category").exists())
                .andExpect(jsonPath("$.fieldErrors.durationTime").exists())
                .andExpect(jsonPath("$.fieldErrors.strtDate").exists())
                .andExpect(jsonPath("$.fieldErrors.endDate").exists());
    }

    @Test
    void getHabitWithAuthenticatedUserId() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(2L);

        HabitDTO habit = new HabitDTO();
        habit.setId(1L);
        habit.setUserId(2L);
        habit.setCategory("INTELLIGENCE");
        habit.setTitle("test");
        habit.setContent("test case");
        habit.setDurationTime(30);
        habit.setStrtDate(LocalDate.of(2026, 5, 26));
        habit.setEndDate(LocalDate.of(2026, 5, 31));

        given(habitService.getTodayHabits(2L)).willReturn(List.of(habit));

        mockMvc.perform(get("/api/habits")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(2))
                .andExpect(jsonPath("$[0].title").value("test"));

        then(habitService)
                .should(times(1))
                .getTodayHabits(2L);
    }
}
