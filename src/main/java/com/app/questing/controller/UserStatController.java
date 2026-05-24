package com.app.questing.controller;

import com.app.questing.dto.stat.UserStatResponse;
import com.app.questing.service.UserStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class UserStatController {

    private final UserStatService userStatService;

    @GetMapping
    public UserStatResponse getUserStat(@RequestAttribute Long userId){
        return userStatService.getUserStat(userId);
    }
}
