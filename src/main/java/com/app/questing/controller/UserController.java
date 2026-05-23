package com.app.questing.controller;

import com.app.questing.dto.auth.LoginResponse;
import com.app.questing.dto.auth.UserLoginRequest;
import com.app.questing.dto.user.UserResponse;
import com.app.questing.dto.user.UserSignupRequest;
import com.app.questing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserSignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request);
    }


}
