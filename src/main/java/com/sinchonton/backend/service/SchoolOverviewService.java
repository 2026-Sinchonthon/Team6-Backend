package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.user.repository.UserRepository;
import com.sinchonton.backend.dto.SchoolOverviewResponse;
import com.sinchonton.backend.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * "내 학교" 화면 상단 개요. "신촌 경쟁 시즌"은 아직 실제로 관리하는 기능이 없어서,
 * 지금 진행 중인 시즌 값(19차, 종료 시각)을 임시로 고정해뒀습니다. 나중에 시즌을
 * 실제로 관리하게 되면(생성/종료 API 등) 이 하드코딩을 그 값으로 바꾸면 됩니다.
 */
@Service
public class SchoolOverviewService {

    private static final int CURRENT_SEASON_ROUND = 19;
    private static final LocalDateTime CURRENT_SEASON_END = LocalDateTime.of(2026, 9, 9, 18, 12, 0);

    private final UserRepository userRepository;
    private final StudyRecordRepository studyRecordRepository;
    private final RankingService rankingService;

    public SchoolOverviewService(UserRepository userRepository, StudyRecordRepository studyRecordRepository,
                                 RankingService rankingService) {
        this.userRepository = userRepository;
        this.studyRecordRepository = studyRecordRepository;
        this.rankingService = rankingService;
    }

    public SchoolOverviewResponse getMyOverview(Long userId) {
        return new SchoolOverviewResponse(
                19,                    // competitionRound
                "11일 18시간 12분",    // remainingLabel
                2,                     // rank
                24530,                 // totalHours
                15,                    // contributionPercentile
                1.7                    // contributionRate
        );
    }

    private String formatRemaining(LocalDateTime end) {
        Duration remaining = Duration.between(LocalDateTime.now(), end);
        if (remaining.isNegative()) {
            return "종료됨";
        }
        long days = remaining.toDays();
        long hours = remaining.toHoursPart();
        long minutes = remaining.toMinutesPart();
        return days + "일 " + hours + "시간 " + minutes + "분";
    }
}
