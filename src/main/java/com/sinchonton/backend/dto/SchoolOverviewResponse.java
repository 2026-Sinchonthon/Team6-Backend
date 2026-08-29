package com.sinchonton.backend.dto;

public record SchoolOverviewResponse(
        int competitionRound,
        String remainingLabel,
        int rank,
        long totalHours,
        int contributionPercentile,
        double contributionRate
) {
}
