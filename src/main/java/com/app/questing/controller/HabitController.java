package com.app.questing.controller;

import com.app.questing.dto.HabitCreateRequest;
import com.app.questing.dto.HabitDTO;
import com.app.questing.dto.HabitUpdateRequest;
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
    public List<HabitDTO> getTodayHabits(){return habitService.getTodayHabits();}

    @PostMapping
    public HabitDTO createHabit(@RequestBody HabitCreateRequest request) {
        return habitService.createHabit(request);
    }

    @PutMapping("/{habitId}")
    public HabitDTO updateHabit(@PathVariable Long habitId,
                                @RequestBody HabitUpdateRequest request){
        return habitService.updateHabit(habitId, request);
    }

    @DeleteMapping("/{habitId}")
    public void deleteHabit(@PathVariable Long habitId){habitService.deleteHabit(habitId);}


}
