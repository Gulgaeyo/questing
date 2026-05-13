package com.app.questing.mapper;

import com.app.questing.dto.stat.UserStatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserStatMapper {

    UserStatDTO findByUserId(@Param("userId") Long userId);

    int updateStrength(UserStatDTO userStat);

    int updateMental(UserStatDTO userStat);

    int updateIntelligence(UserStatDTO userStat);
}
