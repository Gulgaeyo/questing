package com.app.questing.service;

import com.app.questing.dto.stat.UserStatDTO;
import com.app.questing.dto.stat.UserStatResult;
import com.app.questing.mapper.UserStatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatService {

    private static final int MINUTES_PER_STAT = 30;

    private final UserStatMapper userStatMapper;

    public UserStatDTO getUserStat(Long userId){
        return userStatMapper.findByUserId(userId);
    }

    public UserStatResult addProgress(Long userId, String category, Integer durationTime){
        UserStatDTO userStat = userStatMapper.findByUserId(userId);

        if(userStat == null){
            throw new IllegalArgumentException("사용자 스탯 정보가 존재하지 않습니다.");
        }

        return switch (category) {
            case "STRENGTH" -> addStrength(userStat, durationTime);
            case "MENTAL" -> addMental(userStat, durationTime);
            case "INTELLIGENCE" -> addIntelligence(userStat, durationTime);
            default -> throw new IllegalArgumentException("지원하지 않는 카테고리입니다.");
        };
    }

    private UserStatResult addStrength(UserStatDTO userStat, Integer durationTime){
        int totalMinute = userStat.getStrMin() + durationTime;
        // 얻은 스탯을 보여주는 것보다 이전 스탯과 바뀐 stat의 차이를 보여주는게 더 좋을 듯함
        int gainedStat = totalMinute/MINUTES_PER_STAT;
        // 다음 스탯 업까지 남은 시간 보여주는 것은 좋음
        int remainingMinutes = totalMinute % MINUTES_PER_STAT;

        userStat.setStrStat(userStat.getStrStat() + gainedStat);
        userStat.setStrMin(remainingMinutes);

        userStatMapper.updateStrength(userStat);

        return result("STRENGTH", durationTime, gainedStat, remainingMinutes, userStat.getStrStat());
    }

    private UserStatResult addMental(UserStatDTO userStat, Integer durationTime){
        int totalMinute = userStat.getMenMin() + durationTime;
        // 얻은 스탯을 보여주는 것보다 이전 스탯과 바뀐 stat의 차이를 보여주는게 더 좋을 듯함
        int gainedStat = totalMinute/MINUTES_PER_STAT;
        // 다음 스탯 업까지 남은 시간 보여주는 것은 좋음
        int remainingMinutes = totalMinute % MINUTES_PER_STAT;

        userStat.setMenStat(userStat.getMenStat() + gainedStat);
        userStat.setMenMin(remainingMinutes);

        userStatMapper.updateMental(userStat);

        return result("MENTAL", durationTime, gainedStat, remainingMinutes, userStat.getMenStat());
    }

    private UserStatResult addIntelligence(UserStatDTO userStat, Integer durationTime){
        int totalMinute = userStat.getIntMin() + durationTime;
        // 얻은 스탯을 보여주는 것보다 이전 스탯과 바뀐 stat의 차이를 보여주는게 더 좋을 듯함
        int gainedStat = totalMinute/MINUTES_PER_STAT;
        // 다음 스탯 업까지 남은 시간 보여주는 것은 좋음
        int remainingMinutes = totalMinute % MINUTES_PER_STAT;

        userStat.setIntStat(userStat.getIntStat() + gainedStat);
        userStat.setIntMin(remainingMinutes);

        userStatMapper.updateIntelligence(userStat);

        return result("INTELLIGENCE", durationTime, gainedStat, remainingMinutes, userStat.getIntStat());
    }

    private UserStatResult result(String category,
                                  Integer addMinutes,
                                  Integer gainedStat,
                                  Integer remainingMinutes,
                                  Integer currentStat){
        UserStatResult result = new UserStatResult();

        result.setCategory(category);
        result.setAddedMinutes(addMinutes);
        result.setGainedStat(gainedStat);
        result.setRemainingMinutes(remainingMinutes);
        result.setCurrentStat(currentStat);

        return result;
    }
}
