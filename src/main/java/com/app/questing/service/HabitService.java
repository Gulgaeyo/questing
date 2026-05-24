package com.app.questing.service;

import com.app.questing.dto.habit.*;
import com.app.questing.dto.quest.QuestCompleteResponse;
import com.app.questing.dto.stat.UserStatResult;
import com.app.questing.exception.ResourceNotFoundException;
import com.app.questing.mapper.HabitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitMapper habitMapper;
    private final UserStatService userStatService;
    private final RewardMessageService rewardMessageService;


    public List<HabitDTO> getTodayHabits(Long userId){

        return habitMapper.getTodayHabits(userId)
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public HabitDTO createHabit(Long userId,
                                HabitCreateRequest request){
        HabitDTO habit = new HabitDTO();

        habit.setUserId(userId);
        habit.setTitle(request.getTitle());
        habit.setContent(request.getContent());
        habit.setCategory(request.getCategory());
        habit.setDurationTime(request.getDurationTime());
        habit.setStrtDate(request.getStrtDate());
        habit.setEndDate(request.getEndDate());
        habit.setCreatedAt(LocalDateTime.now());

        habitMapper.insertHabit(habit);

        HabitDTO savedHabit = habitMapper.findHabitById(habit.getId(), userId);

        return toResponse(savedHabit);
    }

    public HabitDTO updateHabit(Long userId, Long habitId, HabitUpdateRequest request){
        HabitDTO habit = habitMapper.findHabitById(habitId, userId);

        if(habit == null){
            throw new ResourceNotFoundException("존재하지 않는 Habit입니다.");
        }

        habit.setTitle(request.getTitle());
        habit.setCategory(request.getCategory());
        habit.setContent(request.getContent());
        habit.setDurationTime(request.getDurationTime());
        habit.setStrtDate(request.getStrtDate());
        habit.setEndDate(request.getEndDate());

        int updatedCount = habitMapper.updateHabit(habit);

        if(updatedCount == 0){
            throw new ResourceNotFoundException("Habit 수정에 실패했습니다.");
        }

        HabitDTO updatedHabit = habitMapper.findHabitById(habitId, userId);

        return toResponse(updatedHabit);

    }

    public void deleteHabit(Long userId, Long habitId){
        int deletedCount = habitMapper.deleteHabit(habitId, userId);

        if(deletedCount == 0){
            throw new ResourceNotFoundException("존재하지 않는 Habit입니다.");
        }
    }


    private HabitDTO toResponse(HabitDTO habit){
        HabitDTO response = new HabitDTO();

        response.setId(habit.getId());
        response.setUserId(habit.getUserId());
        response.setTitle(habit.getTitle());
        response.setContent(habit.getContent());
        response.setCategory(habit.getCategory());
        response.setDurationTime(habit.getDurationTime());
        response.setStrtDate(habit.getStrtDate());
        response.setEndDate(habit.getEndDate());
        response.setCreatedAt(habit.getCreatedAt());
        response.setUpdatedAt(habit.getUpdatedAt());

        return response;

    }

    //Habit Log Service
    public QuestCompleteResponse completeHabit(Long userId, Long habitId){
        LocalDate today = LocalDate.now();

        HabitDTO habit = habitMapper.findHabitById(habitId, userId);

        if(habit == null) {
            throw new ResourceNotFoundException("존재하지 않는 Habit입니다.");
        }

        HabitLogDTO todayLog = habitMapper.findTodayHabitLog(userId, habitId, today);

        if(todayLog != null) {
            return toAlreadyCompletedResponse(todayLog);
        }

        HabitLogDTO habitLog = new HabitLogDTO();
        habitLog.setUserId(habit.getUserId());
        habitLog.setHabitId(habitId);
        habitLog.setCompletedDate(today);
        habitLog.setCategory(habit.getCategory());
        habitLog.setDurationTime(habit.getDurationTime());
        habitLog.setEarnedExp(habit.getDurationTime());

        habitMapper.insertHabitLog(habitLog);

        //User Stat Service
        UserStatResult statResult = userStatService.addProgress(
                habit.getUserId(),
                habit.getCategory(),
                habit.getDurationTime()
        );

        HabitLogDTO savedHabitLog = habitMapper.findTodayHabitLog(userId, habitId, today);

        return toCompleteResponse(habitId, savedHabitLog, true, statResult);
    }

    private QuestCompleteResponse toCompleteResponse(Long habitId,
                                                     HabitLogDTO habitLog,
                                                     boolean isCompleted,
                                                     UserStatResult statResult) {
        QuestCompleteResponse response = new QuestCompleteResponse();

        response.setQuestType("HABIT");
        response.setQuestId(habitId);

        response.setIsCompleted(isCompleted);
        response.setCompletedDate(habitLog.getCompletedDate());
        response.setCompletedAt(habitLog.getCompletedAt());

        response.setCategory(habitLog.getCategory());
        response.setDurationTime(habitLog.getDurationTime());
        response.setEarnedExp(habitLog.getEarnedExp());

        response.setGainedStat(statResult.getGainedStat());
        response.setRemainingMinutes(statResult.getRemainingMinutes());
        response.setCurrentStat(statResult.getCurrentStat());

        response.setMessage(rewardMessageService.createRewardMessage(statResult));

        return response;
    }

    // 이미 완료된 Habit response
    private QuestCompleteResponse toAlreadyCompletedResponse(HabitLogDTO habitLog) {
        QuestCompleteResponse response = new QuestCompleteResponse();

        response.setQuestType("HABIT");
        response.setQuestId(habitLog.getHabitId());

        response.setIsCompleted(true);
        response.setCompletedDate(habitLog.getCompletedDate());
        response.setCompletedAt(habitLog.getCompletedAt());

        response.setCategory(habitLog.getCategory());
        response.setDurationTime(habitLog.getDurationTime());
        response.setEarnedExp(habitLog.getEarnedExp());

        response.setGainedStat(0);
        response.setRemainingMinutes(null);
        response.setCurrentStat(null);
        response.setMessage(rewardMessageService.createAlreadyCompletedMessage());

        return response;
    }



    //Today Habit Service
    public List<HabitTodayResponse> getTodayHabitsWishCompletion(Long userId){
        return habitMapper.findTodayHabitsWithCompletion(userId);
    }


}
