package com.sinchonton.backend.dto;

import lombok.Getter;

@Getter
public class SchoolRankingResponse {
    private final Long schoolId;
    private final String schoolName;
    private final Long totalSeconds;
    private final long totalStudyMinutes;
    private final double totalStudyHours;
    private final int rank;

    public SchoolRankingResponse(Long schoolId, String schoolName, Long totalSeconds, int rank) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.totalSeconds = totalSeconds;
        this.totalStudyMinutes = totalSeconds / 60;
        this.totalStudyHours = Math.round((totalSeconds / 3600.0) * 10) / 10.0; // 소수점 1자리
        this.rank = rank;
    }
}