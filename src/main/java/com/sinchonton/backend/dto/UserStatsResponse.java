package com.sinchonton.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStatsResponse {
    private Long userId;
    private String nickname;
    private long totalSeconds;       // 전체 누적 공부시간
    private long bestSessionSeconds; // 한 번에 가장 오래 공부한 기록
    private int totalSessions;       // 총 몇 번 공부했는지
    private long dailyAverageSeconds; // 실제로 공부한 날 기준 일평균
    private int departmentPercentile; // 학과 내 상위 N% (낮을수록 상위권)
}
