package com.sinchonton.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DepartmentRankingResponse {
    private final Long departmentId;
    private final String name;
    private final Long totalSeconds;
    private final long totalStudyMinutes;
    private final double totalStudyHours;
    private final int activeUserCount;
    private final int rank;
    private final boolean isMine;

    public DepartmentRankingResponse(Long departmentId, String name, Long totalSeconds,
                                     int activeUserCount, int rank, boolean isMine) {
        this.departmentId = departmentId;
        this.name = name;
        this.totalSeconds = totalSeconds;
        this.totalStudyMinutes = totalSeconds / 60;
        this.totalStudyHours = Math.round((totalSeconds / 3600.0) * 10) / 10.0;
        this.activeUserCount = activeUserCount;
        this.rank = rank;
        this.isMine = isMine;
    }

    /** Lombok 기본 getter 대신 직접 이름을 고정해서 Jackson이 "isMine"으로 직렬화하게 합니다. */
    @JsonProperty("isMine")
    public boolean isMine() {
        return isMine;
    }
}
