package com.app.questing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HabitUpdateRequest {

    private Long userId;
    private String title;
    private String content;
    private String category;
    private LocalDate strtDate;
    private LocalDate endDate;

}
