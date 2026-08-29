package com.sinchonton.backend.service;

import com.sinchonton.backend.dto.UserStatsResponse;
import com.sinchonton.backend.entity.StudyRecord;
import com.sinchonton.backend.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;

import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;

import java.util.List;

@Service
public class UserStatsService {

    private final StudyRecordRepository studyRecordRepository;
    private final UserRepository userRepository;

    public UserStatsService(StudyRecordRepository studyRecordRepository, UserRepository userRepository) {
        this.studyRecordRepository = studyRecordRepository;
        this.userRepository = userRepository;
    }

    public UserStatsResponse getStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다: " + userId));

        List<StudyRecord> records = studyRecordRepository.findByUserId(userId);

        long totalSeconds = records.stream()
                .filter(r -> r.getDurationSeconds() != null)
                .mapToLong(StudyRecord::getDurationSeconds)
                .sum();

        long bestSessionSeconds = records.stream()
                .filter(r -> r.getDurationSeconds() != null)
                .mapToLong(StudyRecord::getDurationSeconds)
                .max()
                .orElse(0L);

        int totalSessions = (int) records.stream()
                .filter(r -> r.getDurationSeconds() != null)
                .count();

        return new UserStatsResponse(
                user.getId(),
                user.getNickname(),
                totalSeconds,
                bestSessionSeconds,
                totalSessions
        );
    }
}