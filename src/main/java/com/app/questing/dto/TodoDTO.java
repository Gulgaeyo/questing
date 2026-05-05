package com.app.questing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TodoDTO {
    private Long id;
    private Long userId;
    private LocalDate todoDate;
    private String timeSlot;
    private String title;
    private String category;
    private Integer durationTime;
    private Boolean completed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
