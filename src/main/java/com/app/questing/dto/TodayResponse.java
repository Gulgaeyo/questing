package com.app.questing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TodayResponse {

    private LocalDate today;
    private List<TodoResponse> todos;
    private List<HabitTodayResponse> habits;

}
