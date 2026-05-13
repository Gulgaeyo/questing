package com.app.questing.dto.quest;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QuestCompleteResponse{

    private String questType;
    private Long questId;

    private Boolean isCompleted;

    private LocalDate completedDate;
    private LocalDateTime completedAt;

    private String category;
    private Integer durationTime;
    private Integer earnedExp;

    private Integer gainedStat;
    private Integer remainingMinutes;
    private Integer currentStat;

    private String message;
}
