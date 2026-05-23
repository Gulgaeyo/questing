package com.app.questing.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String loginId;
    private String password;
    private String userName;
    private String nickName;
    private String email;
    private LocalDate birth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
