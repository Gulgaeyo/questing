package com.app.questing.dto.todo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoUpdateRequest {

    private Long userId;

    @NotBlank(message = "시간대는 필수입니다.")
    private String timeSlot;

    @NotBlank(message = "TODO 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "진행 시간은 필수입니다.")
    @Min(value = 1, message = "진행 시간은 1분 이상이어야 합니다.")
    private Integer durationTime;
}
