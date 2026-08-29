package com.sinchonton.backend.dto;

/**
 * 홈 화면 상단 3칸(신촌 5개 대학 중 우리 학교 / 우리 학교 내 단과대 / 단과대 내 내 순위)용 요약.
 */
public record RankingSummaryResponse(
        SchoolRankEntry schoolRank,
        CollegeRankEntry collegeRank,
        MyRankEntry myRank
) {
    public record SchoolRankEntry(int rank, long totalHours) {
    }

    public record CollegeRankEntry(int rank, long totalHours) {
    }

    public record MyRankEntry(int rank, long cumulativeHours) {
    }
}
