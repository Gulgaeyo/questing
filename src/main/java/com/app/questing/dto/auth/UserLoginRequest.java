package com.app.questing.dto.auth;

import lombok.Data;

@Data
public class UserLoginRequest {

    public String loginId;
    public String password;

}
