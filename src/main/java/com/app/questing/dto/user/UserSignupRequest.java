package com.app.questing.dto.user;

import lombok.Data;

@Data
public class UserSignupRequest {

    private String loginId;
    private String password;
    private String userName;
    private String nickName;
    private String birth;
    private String email;

}
