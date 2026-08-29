package com.sinchonton.backend.service;

import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.dto.UserStatsResponse;
import com.sinchonton.backend.entity.StudyRecord;
import com.sinchonton.backend.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;

import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserStatsService {

    private final StudyRecordRepository studyRecordRepository;
    private final UserRepository userRepository;
    private final RankingService rankingService;

    public UserStatsService(StudyRecordRepository studyRecordRepository, UserRepository userRepository,
                            RankingService rankingService) {
        this.studyRecordRepository = studyRecordRepository;
        this.userRepository = userRepository;
        this.rankingService = rankingService;
    }

    public UserStatsResponse getStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + userId));

        List<StudyRecord> records = studyRecordRepository.findByUserId(userId);
        List<StudyRecord> completed = records.stream()
                .filter(r -> r.getDurationSeconds() != null)
                .collect(Collectors.toList());

        long totalSeconds = completed.stream().mapToLong(StudyRecord::getDurationSeconds).sum();

        long bestSessionSeconds = completed.stream()
                .mapToLong(StudyRecord::getDurationSeconds)
                .max()
                .orElse(0L);

        int totalSessions = completed.size();

        // 실제로 공부한 날짜 수 기준 일평균 (기록 하루 여러 번 해도 하루로 침)
        Set<java.time.LocalDate> studyDays = completed.stream()
                .filter(r -> r.getStartedAt() != null)
                .map(r -> r.getStartedAt().toLocalDate())
                .collect(Collectors.toSet());
        long dailyAverageSeconds = studyDays.isEmpty() ? 0 : totalSeconds / studyDays.size();

        int departmentPercentile = calculateDepartmentPercentile(user);

        return new UserStatsResponse(
                user.getId(),
                user.getNickname(),
                totalSeconds,
                bestSessionSeconds,
                totalSessions,
                dailyAverageSeconds,
                departmentPercentile
        );
    }

    /** 학과 내 상위 몇 % 인지. 학과 미선택이거나 학과에 본인뿐이면 0(=최상위)으로 처리. */
    private int calculateDepartmentPercentile(User user) {
        if (user.getDepartmentId() == null) {
            return 0;
        }

        List<UserRankingResponse> departmentRanking =
                rankingService.getUserRanking("department", user.getDepartmentId());

        int totalCount = departmentRanking.size();
        if (totalCount == 0) {
            return 0;
        }

        return departmentRanking.stream()
                .filter(r -> r.getUserId().equals(user.getId()))
                .findFirst()
                .map(r -> (int) Math.round(r.getRank() * 100.0 / totalCount))
                .orElse(0);
    }
}
