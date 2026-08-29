package com.sinchonton.backend.repository;

import com.sinchonton.backend.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {

    boolean existsByUserIdAndEndedAtIsNull(Long userId);

    Optional<StudyRecord> findByUserIdAndEndedAtIsNull(Long userId);

    List<StudyRecord> findByUserIdAndStartedAtBetween(
            Long userId, LocalDateTime start, LocalDateTime end
    );
}