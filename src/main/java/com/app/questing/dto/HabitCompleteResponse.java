package com.app.questing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HabitCompleteResponse {

    private Long habitId;
    private Boolean isCompleted;
    private LocalDate completedDate;
    private LocalDateTime completedAt;
    private String category;
    private Integer durationTime;
    private Integer earnedExp;

}
