package com.app.questing.service;

import com.app.questing.config.JwtProvider;
import com.app.questing.dto.auth.LoginResponse;
import com.app.questing.dto.auth.UserLoginRequest;
import com.app.questing.dto.user.UserDTO;
import com.app.questing.dto.user.UserResponse;
import com.app.questing.dto.user.UserSignupRequest;
import com.app.questing.exception.DuplicateUserException;
import com.app.questing.exception.LoginFailedException;
import com.app.questing.mapper.UserMapper;
import com.app.questing.mapper.UserStatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserStatMapper userStatMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signup(UserSignupRequest request){
        validateDuplicateUser(request);

        UserDTO user = new UserDTO();
        user.setLoginId(request.getLoginId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserName(request.getUserName());
        user.setNickName(request.getNickName());
        user.setEmail(request.getEmail());
        user.setBirth(request.getBirth());

        userMapper.insertUser(user);

        userStatMapper.insertUserStat(user.getId());

        return toResponse(user);
    }

    public LoginResponse login(UserLoginRequest request){
        UserDTO user = userMapper.findByLoginId(request.getLoginId());

        if(user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return toLoginResponse(user);
    }

    private void validateDuplicateUser(UserSignupRequest request){
        if(userMapper.existsByLoginId(request.getLoginId())) {
            throw new DuplicateUserException("이미 사용 중인 아이디 입니다.");
        }
        if(userMapper.existsByNickName(request.getNickName())) {
            throw new DuplicateUserException("이미 사용 중인 닉네임입니다.");
        }
        if(userMapper.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("이미 사용 중인 이메일입니다.");
        }
    }

    private UserResponse toResponse(UserDTO user){
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setLoginId(user.getLoginId());
        response.setUserName(user.getUserName());
        response.setNickName(user.getNickName());
        response.setEmail(user.getEmail());
        response.setBirth(user.getBirth());

        return response;
    }

    private LoginResponse toLoginResponse(UserDTO user){
        LoginResponse response = new LoginResponse();

        response.setId(user.getId());
        response.setLoginId(user.getLoginId());
        response.setNickName(user.getNickName());
        response.setEmail(user.getEmail());
        response.setAccessToken(jwtProvider.createAccessToken(user));

        return response;
    }

}
