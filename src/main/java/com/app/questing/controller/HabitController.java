package com.app.questing.controller;

import com.app.questing.dto.habit.HabitCreateRequest;
import com.app.questing.dto.habit.HabitDTO;
import com.app.questing.dto.habit.HabitUpdateRequest;
import com.app.questing.dto.quest.QuestCompleteResponse;
import com.app.questing.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @GetMapping
    public List<HabitDTO> getTodayHabits(@RequestAttribute Long userId){
        return habitService.getTodayHabits(userId);
    }

    @PostMapping
    public HabitDTO createHabit(@RequestAttribute Long userId,
                                @RequestBody HabitCreateRequest request) {
        return habitService.createHabit(userId, request);
    }

    @PutMapping("/{habitId}")
    public HabitDTO updateHabit(@RequestAttribute Long userId,
                                @PathVariable Long habitId,
                                @RequestBody HabitUpdateRequest request){
        return habitService.updateHabit(userId, habitId, request);
    }

    @DeleteMapping("/{habitId}")
    public void deleteHabit(@RequestAttribute Long userId,
                            @PathVariable Long habitId){
        habitService.deleteHabit(userId, habitId);
    }

    @PatchMapping("/{habitId}/complete")
    public QuestCompleteResponse completeHabit(@RequestAttribute Long userId,
                                               @PathVariable Long habitId){
        return habitService.completeHabit(userId, habitId);
    }


}
