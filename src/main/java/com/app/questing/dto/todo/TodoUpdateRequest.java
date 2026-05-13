package com.app.questing.dto.todo;

import lombok.Data;

@Data
public class TodoUpdateRequest {
    private Long userId;
    private String timeSlot;
    private String title;
    private String category;
    private Integer durationTime;
}
