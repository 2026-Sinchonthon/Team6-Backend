package com.sinchonton.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StudyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long durationSeconds;

    private LocalDateTime pausedAt;              // null이 아니면 현재 일시정지 중
    private Long totalPausedSeconds = 0L;         // 지금까지 일시정지했던 시간 누적 합계
}