package com.app.questing.mapper;

import com.app.questing.dto.user.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    UserDTO findUserById(@Param("id") Long id);

    UserDTO findByLoginId(@Param("loginId") String loginId);

    UserDTO findByNickName(@Param("nickName") String nickName);

    UserDTO findByEmail(@Param("email") String email);

    // 중복 검증용
    boolean existsByLoginId(@Param("loginId") String loginId);
    boolean existsByEmail(@Param("email") String email);
    boolean existsByNickName(@Param("nickName") String nickName);

    void insertUser(UserDTO user);

}
