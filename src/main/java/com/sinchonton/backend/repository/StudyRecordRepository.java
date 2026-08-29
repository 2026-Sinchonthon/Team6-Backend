package com.sinchonton.backend.repository;

import com.sinchonton.backend.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {

    boolean existsByUserIdAndEndedAtIsNull(Long userId);

    Optional<StudyRecord> findByUserIdAndEndedAtIsNull(Long userId);

    List<StudyRecord> findByUserIdAndStartedAtBetween(
            Long userId, LocalDateTime start, LocalDateTime end
    );
    List<StudyRecord> findByUserId(Long userId);

    // 새로 추가: 유저별 총 공부시간 합계 (랭킹 계산용)
    @Query("SELECT s.userId AS userId, SUM(s.durationSeconds) AS totalSeconds " +
            "FROM StudyRecord s " +
            "WHERE s.durationSeconds IS NOT NULL " +
            "GROUP BY s.userId")
    List<UserDurationSum> sumDurationGroupByUser();

    // 위 쿼리 결과를 담을 인터페이스 (Spring Data JPA의 "Projection" 기능)
    interface UserDurationSum {
        Long getUserId();
        Long getTotalSeconds();
    }

    // "현재 N명 집중 중" 표시용. 타이머가 안 끝났고(endedAt IS NULL) 일시정지도 아닌
    // (pausedAt IS NULL) 유저만 "집중 중"으로 센다 — 일시정지 중인 유저까지 넣으면
    // 문구 의미랑 안 맞아서 제외했다.
    @Query("SELECT DISTINCT s.userId FROM StudyRecord s WHERE s.endedAt IS NULL AND s.pausedAt IS NULL")
    List<Long> findActiveUserIds();
}