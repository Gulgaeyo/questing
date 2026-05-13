package com.app.questing.dto.todo;

import lombok.Data;

@Data
public class TodoCreateRequest {
    private String timeSlot;
    private String title;
    private String category;
    private Integer durationTime;
}
