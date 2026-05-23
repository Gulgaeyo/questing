package com.app.questing.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String loginId;
    private String userName;
    private String nickName;
    private String email;
    private LocalDate birth;
}
