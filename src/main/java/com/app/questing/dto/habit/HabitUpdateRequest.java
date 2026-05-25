package com.app.questing.dto.habit;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HabitUpdateRequest {

    private Long userId;

    @NotBlank(message = "Habit 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "진행 시간은 필수입니다.")
    @Min(value = 1, message = "진행 시간은 1분 이상이어야 합니다.")
    private Integer durationTime;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate strtDate;

    @FutureOrPresent(message = "종료일은 오늘 또는 미래 날짜만 가능합니다.")
    private LocalDate endDate;

}
