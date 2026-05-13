package com.app.questing.dto.habit;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class HabitLogDTO {

    private Long id;
    private Long userId;
    private Long habitId;
    private LocalDate completedDate;
    private LocalDateTime completedAt;
    private String category;
    private Integer durationTime;
    private Integer earnedExp;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
