package com.app.questing.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    String findUserById(Long id);
    String findUserByLoginId(String loginId);

}
