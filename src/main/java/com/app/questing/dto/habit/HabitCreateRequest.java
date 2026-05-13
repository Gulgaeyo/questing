package com.app.questing.dto.habit;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HabitCreateRequest {

    private String title;
    private String content;
    private String category;
    private Integer durationTime;
    private LocalDate strtDate;
    private LocalDate endDate;
}
