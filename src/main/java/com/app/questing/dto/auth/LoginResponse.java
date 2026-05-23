package com.app.questing.dto.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private Long id;
    private String loginId;
    private String nickName;
    private String email;
    private String accessToken;

}
