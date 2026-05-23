package com.app.questing.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSignupRequest {

    private String loginId;
    private String password;
    private String userName;
    private String nickName;
    private LocalDate birth;
    private String email;

}
