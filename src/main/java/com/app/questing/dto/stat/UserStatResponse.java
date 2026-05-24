package com.app.questing.dto.stat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserStatResponse {

    private Long userId;
    private Integer strengthStat;
    private Integer strengthMinutes;
    private Integer mentalStat;
    private Integer mentalMinutes;
    private Integer intelligenceStat;
    private Integer intelligenceMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
