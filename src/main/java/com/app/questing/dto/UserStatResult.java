package com.app.questing.dto;

import lombok.Data;

@Data
public class UserStatResult {

    private String category;
    private Integer addedMinutes;
    private Integer gainedStat;
    private Integer remainingMinutes;
    private Integer currentStat;
}
