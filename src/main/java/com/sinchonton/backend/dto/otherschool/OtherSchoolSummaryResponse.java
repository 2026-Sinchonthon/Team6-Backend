package com.sinchonton.backend.dto.otherschool;

import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStat;

public record OtherSchoolSummaryResponse(
        Long schoolId,
        String schoolName,
        Double latitude,
        Double longitude,
        Long totalStudyMinutes,
        Integer activeUserCount
) {

    public static OtherSchoolSummaryResponse from(School school, SchoolStudyStat stat) {
        return new OtherSchoolSummaryResponse(
                school.getId(),
                school.getName(),
                stat.getLatitude(),
                stat.getLongitude(),
                stat.getTotalStudyMinutes(),
                stat.getActiveUserCount()
        );
    }
}
