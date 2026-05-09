package com.app.questing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HabitCreateRequest {

    private String title;
    private String content;
    private String category;
    private Integer durationTime;
    private LocalDate strtDate;
    private LocalDate endDate;
}
