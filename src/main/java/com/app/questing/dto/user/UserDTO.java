package com.app.questing.dto.user;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String loginId;
    private String userName;
    private String nickName;
    private String email;
    private String birth;

}
