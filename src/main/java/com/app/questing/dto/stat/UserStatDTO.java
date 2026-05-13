package com.app.questing.dto.stat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserStatDTO {

    private Long userId;
    private Integer strStat;
    private Integer strMin;
    private Integer menStat;
    private Integer menMin;
    private Integer intStat;
    private Integer intMin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
