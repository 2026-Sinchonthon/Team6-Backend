package com.sinchonton.backend.domain.mypage.dto;

public record MyPageStudyRecordResponse(
        String departmentName,
        Integer departmentPercentile,
        String departmentPercentileLabel,
        Long totalStudyMinutes,
        String totalTime,
        Long dailyAverageMinutes,
        String dailyAverage
) {
}
