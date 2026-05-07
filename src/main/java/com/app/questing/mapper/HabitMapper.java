package com.app.questing.mapper;

import com.app.questing.dto.HabitDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HabitMapper {

    List<HabitDTO> getTodayHabits(@Param("userId") Long userId,
                                  @Param("strtDate") LocalDate today);
    HabitDTO findHabitById(@Param("id") Long id,
                           @Param("userId") Long userId);

    List<HabitDTO> findHabitByCategory(@Param("userId") Long userId,
                                       @Param("category") String category);

    List<HabitDTO> findHabitByDate(@Param("userId") Long userId,
                                   @Param("strtDate") String startDate,
                                   @Param("endDate") String endDate);

    void insertHabit(HabitDTO habit);

    int updateHabit(HabitDTO habit);

    int deleteHabit(@Param("id") Long id,
                    @Param("userId") Long userId);
}
