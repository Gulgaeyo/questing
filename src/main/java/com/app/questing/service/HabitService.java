package com.app.questing.service;

import com.app.questing.dto.HabitCreateRequest;
import com.app.questing.dto.HabitDTO;
import com.app.questing.dto.HabitUpdateRequest;
import com.app.questing.mapper.HabitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitMapper habitMapper;
    private static final Long TEMP_USER_ID = 1L;


    public List<HabitDTO> getTodayHabits(){
        LocalDate today = LocalDate.now();

        return habitMapper.getTodayHabits(TEMP_USER_ID, today)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public HabitDTO createHabit(HabitCreateRequest request){
        HabitDTO habit = new HabitDTO();

        habit.setUserId(TEMP_USER_ID);
        habit.setTitle(request.getTitle());
        habit.setContent(request.getContent());
        habit.setCategory(request.getCategory());
        habit.setStrtDate(request.getStrtDate());
        habit.setEndDate(request.getEndDate());
        habit.setCreatedAt(LocalDateTime.now());

        habitMapper.insertHabit(habit);

        HabitDTO savedHabit = habitMapper.findHabitById(habit.getId(), TEMP_USER_ID);

        return toResponse(savedHabit);
    }

    public HabitDTO updateHabit(Long habitId, HabitUpdateRequest request){
        HabitDTO habit = habitMapper.findHabitById(habitId, TEMP_USER_ID);

        if(habit == null){
            throw new IllegalArgumentException("존재하지 않는 Habit입니다.");
        }

        habit.setTitle(request.getTitle());
        habit.setCategory(request.getCategory());
        habit.setContent(request.getContent());
        habit.setStrtDate(request.getStrtDate());
        habit.setEndDate(request.getEndDate());

        int updatedCount = habitMapper.updateHabit(habit);

        if(updatedCount == 0){
            throw new IllegalArgumentException("Habit 수정에 실패했습니다.");
        }

        HabitDTO updatedHabit = habitMapper.findHabitById(habitId, TEMP_USER_ID);

        return toResponse(updatedHabit);

    }

    public void deleteHabit(Long habitId){
        int deletedCount = habitMapper.deleteHabit(habitId, TEMP_USER_ID);

        if(deletedCount == 0){
            throw new IllegalArgumentException("존재하지 않는 Habit입니다.");
        }
    }


    private HabitDTO toResponse(HabitDTO habit){
        HabitDTO response = new HabitDTO();

        response.setId(habit.getId());
        response.setUserId(habit.getUserId());
        response.setTitle(habit.getTitle());
        response.setContent(habit.getContent());
        response.setCategory(habit.getCategory());
        response.setStrtDate(habit.getStrtDate());
        response.setEndDate(habit.getEndDate());
        response.setCreatedAt(habit.getCreatedAt());
        response.setUpdatedAt(habit.getUpdatedAt());

        return response;

    }
}
