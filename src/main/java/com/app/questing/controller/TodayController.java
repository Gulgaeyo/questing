package com.app.questing.controller;

import com.app.questing.dto.today.TodayResponse;
import com.app.questing.service.TodayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/today")
@RequiredArgsConstructor
public class TodayController {

    private final TodayService todayService;

    @GetMapping
    public TodayResponse getToday(){
        return todayService.getToday();
    }
}
