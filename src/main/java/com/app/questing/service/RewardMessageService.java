package com.app.questing.service;

import com.app.questing.dto.UserStatResult;
import org.springframework.stereotype.Service;

@Service
public class RewardMessageService {

    private static final int MINUTES_PER_STAT = 30;

    public String createRewardMessage(UserStatResult statResult){
        String statName = toKoreanStatName(statResult.getCategory());

        if(statResult.getGainedStat() > 0) {
            return "퀘스트 완료. " + statName + " 경험치 "
                    + statResult.getAddedMinutes() + "분 누적. 다음 성장까지 "
                    + (30 - statResult.getRemainingMinutes()) + "분 남았어.";
        }

        return "퀘스트 완료. " + statName + " 경험치 "
                + statResult.getAddedMinutes() + "분 누적. 다음 성장까지 "
                + (30 - statResult.getRemainingMinutes()) + "분 남았어.";
    }

    public String createAlreadyCompletedMessage() {
        return "오늘은 이미 완료한 퀘스트야. 내일 다시 진행할 수 있어.";
    }

    private int getMinutesToNextGrowth(UserStatResult statResult){
        int remaining = statResult.getRemainingMinutes();

        if (remaining == 0) {
            return MINUTES_PER_STAT;
        };

        return MINUTES_PER_STAT - remaining;
    }

    private String toKoreanStatName(String category) {
        return switch (category){
            case "STRENGTH" -> "힘";
            case "MENTAL" -> "정신";
            case "INTELLIGENCE" -> "지능";
            default -> "알 수 없는 스탯";
        };
    }
}
