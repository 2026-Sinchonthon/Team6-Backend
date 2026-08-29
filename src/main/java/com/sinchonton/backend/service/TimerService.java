package com.sinchonton.backend.service;

import com.sinchonton.backend.entity.StudyRecord;
import com.sinchonton.backend.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimerService {

    private final StudyRecordRepository studyRecordRepository;

    public TimerService(StudyRecordRepository studyRecordRepository) {
        this.studyRecordRepository = studyRecordRepository;
    }

    @Transactional
    public void startTimer(Long userId) {
        if (studyRecordRepository.existsByUserIdAndEndedAtIsNull(userId)) {
            throw new IllegalStateException("이미 진행 중인 타이머가 있습니다.");
        }
        StudyRecord record = new StudyRecord();
        record.setUserId(userId);
        record.setStartedAt(LocalDateTime.now());
        studyRecordRepository.save(record);
    }

    @Transactional
    public void stopTimer(Long userId) {
        StudyRecord record = studyRecordRepository
                .findByUserIdAndEndedAtIsNull(userId)
                .orElseThrow(() -> new IllegalStateException("진행 중인 타이머가 없습니다."));

        record.setEndedAt(LocalDateTime.now());
        long seconds = Duration.between(record.getStartedAt(), record.getEndedAt()).getSeconds();
        record.setDurationSeconds(seconds);
    }

    public long getTodayTotalSeconds(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<StudyRecord> records = studyRecordRepository
                .findByUserIdAndStartedAtBetween(userId, startOfDay, endOfDay);

        return records.stream()
                .filter(r -> r.getDurationSeconds() != null)
                .mapToLong(StudyRecord::getDurationSeconds)
                .sum();
    }
}