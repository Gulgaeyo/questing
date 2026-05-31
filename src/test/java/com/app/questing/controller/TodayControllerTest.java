package com.app.questing.controller;

import com.app.questing.config.JwtProvider;
import com.app.questing.dto.habit.HabitTodayResponse;
import com.app.questing.dto.today.TodayResponse;
import com.app.questing.dto.todo.TodoResponse;
import com.app.questing.service.TodayService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodayController.class)
public class TodayControllerTest {

    @MockitoBean
    private TodayService todayService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getTodayWithAuthenticatedUserId() throws Exception {
        given(jwtProvider.getUserId("valid-token")).willReturn(2L);

        TodoResponse todo = new TodoResponse();
        todo.setId(1L);
        todo.setUserId(2L);
        todo.setTitle("test case");
        todo.setCategory("INTELLIGENCE");
        todo.setTimeSlot("MORNING");
        todo.setDurationTime(30);
        todo.setIsCompleted(false);

        HabitTodayResponse habit= new HabitTodayResponse();
        habit.setHabitId(1L);
        habit.setUserId(2L);
        habit.setTitle("test Case 2");
        habit.setContent("test content 2");
        habit.setCategory("MENTAL");
        habit.setDurationTime(20);
        habit.setCompletedToday(false);

        TodayResponse today = new TodayResponse();
        today.setToday(LocalDate.of(2026,5,31));
        today.setTodos(List.of(todo));
        today.setHabits(List.of(habit));

        given(todayService.getToday(2L)).willReturn(today);

        mockMvc.perform(get("/api/today")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.today").value("2026-05-31"))
                .andExpect(jsonPath("$.todos[0].id").value(1))
                .andExpect(jsonPath("$.todos[0].userId").value(2))
                .andExpect(jsonPath("$.todos[0].title").value("test case"))
                .andExpect(jsonPath("$.habits[0].habitId").value(1))
                .andExpect(jsonPath("$.habits[0].userId").value(2))
                .andExpect(jsonPath("$.habits[0].title").value("test Case 2"));

        then(todayService)
                .should(times(1))
                .getToday(2L);
    }

}
