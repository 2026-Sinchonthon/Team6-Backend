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
    public void pauseTimer(Long userId) {
        StudyRecord record = getActiveRecord(userId);

        if (record.getPausedAt() != null) {
            throw new IllegalStateException("이미 일시정지된 상태입니다.");
        }

        record.setPausedAt(LocalDateTime.now());
    }

    @Transactional
    public void resumeTimer(Long userId) {
        StudyRecord record = getActiveRecord(userId);

        if (record.getPausedAt() == null) {
            throw new IllegalStateException("일시정지 상태가 아닙니다.");
        }

        long pausedSeconds = Duration.between(record.getPausedAt(), LocalDateTime.now()).getSeconds();
        record.setTotalPausedSeconds(record.getTotalPausedSeconds() + pausedSeconds);
        record.setPausedAt(null);
    }

    @Transactional
    public void stopTimer(Long userId) {
        StudyRecord record = getActiveRecord(userId);

        // 일시정지된 채로 stop을 누른 경우, 마지막 일시정지 구간까지 계산에 반영
        if (record.getPausedAt() != null) {
            long pausedSeconds = Duration.between(record.getPausedAt(), LocalDateTime.now()).getSeconds();
            record.setTotalPausedSeconds(record.getTotalPausedSeconds() + pausedSeconds);
            record.setPausedAt(null);
        }

        record.setEndedAt(LocalDateTime.now());
        long totalElapsed = Duration.between(record.getStartedAt(), record.getEndedAt()).getSeconds();
        long actualStudySeconds = totalElapsed - record.getTotalPausedSeconds();
        record.setDurationSeconds(Math.max(actualStudySeconds, 0)); // 혹시 모를 음수 방지
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

    // start/pause/resume/stop이 공통으로 쓰는 "현재 진행중인 기록 찾기" 로직
    private StudyRecord getActiveRecord(Long userId) {
        return studyRecordRepository
                .findByUserIdAndEndedAtIsNull(userId)
                .orElseThrow(() -> new IllegalStateException("진행 중인 타이머가 없습니다."));
    }
}