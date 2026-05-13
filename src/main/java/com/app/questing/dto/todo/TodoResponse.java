package com.app.questing.dto.todo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TodoResponse {
    private Long Id;
    private Long userId;
    private LocalDate todoDate;
    private String timeSlot;
    private String title;
    private String category;
    private Integer durationTime;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
