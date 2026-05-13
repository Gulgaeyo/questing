package com.app.questing.dto.habit;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HabitDTO {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String category;
    private Integer durationTime;
    private LocalDate strtDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
