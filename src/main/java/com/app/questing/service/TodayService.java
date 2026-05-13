package com.app.questing.service;

import com.app.questing.dto.today.TodayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TodayService {

    private final TodoService todoService;
    private final HabitService habitService;

    public TodayResponse getToday(){
        TodayResponse response = new TodayResponse();

        response.setToday(LocalDate.now());
        response.setTodos(todoService.getTodayTodos());
        response.setHabits(habitService.getTodayHabitsWishCompletion());

        return response;

    }
}
