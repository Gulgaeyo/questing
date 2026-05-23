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

    public TodayResponse getToday(Long userId){
        TodayResponse response = new TodayResponse();

        response.setToday(LocalDate.now());
        response.setTodos(todoService.getTodayTodos(userId));
        response.setHabits(habitService.getTodayHabitsWishCompletion(userId));

        return response;

    }
}
