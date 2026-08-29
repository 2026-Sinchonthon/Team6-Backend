package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;
import com.sinchonton.backend.dto.DepartmentRankingResponse;
import com.sinchonton.backend.dto.SchoolOverviewResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + userId));
        if (user.getSchoolId() == null) {
            throw new IllegalStateException("학교를 먼저 선택해야 합니다.");
        }

        SchoolRankingResponse mySchool = rankingService.getSchoolRanking().stream()
                .filter(r -> r.getSchoolId().equals(user.getSchoolId()))
                .findFirst()
                .orElse(null);

        int rank = mySchool == null ? 0 : mySchool.getRank();
        long totalHours = mySchool == null ? 0 : Math.round(mySchool.getTotalSeconds() / 3600.0);

        int contributionPercentile = 0;
        double contributionRate = 0.0;

        if (user.getCollegeId() != null && user.getDepartmentId() != null) {
            List<UserRankingResponse> deptUsers = rankingService.getUserRanking("department", user.getDepartmentId());
            int totalCount = deptUsers.size();
            UserRankingResponse mine = deptUsers.stream()
                    .filter(r -> r.getUserId().equals(userId))
                    .findFirst()
                    .orElse(null);
            if (mine != null && totalCount > 0) {
                contributionPercentile = (int) Math.round(mine.getRank() * 100.0 / totalCount);
            }

            List<DepartmentRankingResponse> deptRanking =
                    rankingService.getDepartmentRanking(user.getCollegeId(), userId);
            DepartmentRankingResponse myDept = deptRanking.stream()
                    .filter(r -> r.getDepartmentId().equals(user.getDepartmentId()))
                    .findFirst()
                    .orElse(null);
            if (myDept != null && myDept.getTotalSeconds() > 0 && mine != null) {
                contributionRate = Math.round(mine.getTotalSeconds() * 1000.0 / myDept.getTotalSeconds()) / 10.0;
            }
        }

        return new SchoolOverviewResponse(
                CURRENT_SEASON_ROUND,
                formatRemaining(CURRENT_SEASON_END),
                rank,
                totalHours,
                contributionPercentile,
                contributionRate
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
