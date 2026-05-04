package com.app.questing.dto;

import lombok.Data;

@Data
public class TodoUpdateRequest {
    private Long userId;
    private String timeSlot;
    private String title;
    private String category;
    private String durationTime;
}
